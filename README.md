# Anagram Arena API

Backend-first daily anagram game.

Instead of exposing generic CRUD endpoints, this API was designed as a playable product:

- generates the daily board;
- uses one global daily challenge shared by every player;
- validates guesses;
- reveals discovered words to everyone playing that day;
- credits solves to a temporary player tag;
- works both in `Scalar` and directly in the terminal.

Every player sees the same board for the day. When someone discovers a valid word, that answer becomes visible on the shared board for everyone else too.

## Project Snapshot

| Aspect | What it shows |
| --- | --- |
| **Product idea** | A REST API that feels like a game, not just a data source |
| **Experience** | Playable in browser and in `text/plain` terminal mode |
| **Architecture** | Clear separation between `domain`, `application`, and `adapters` |
| **State** | One global daily challenge persisted locally and shared by all players |
| **Docs** | OpenAPI served by the app with Scalar as interactive playground |
| **Extra touch** | Player session lets each correct answer show who found it |

## Tech Stack

| Technology | Why it matters here |
| --- | --- |
| **Java 21** | Modern language baseline |
| **Spring Boot 4** | REST API foundation |
| **Hexagonal-style architecture** | Business rules isolated from infrastructure |
| **Spring Data JPA + PostgreSQL** | Persistent daily challenge state |
| **Flyway** | Versioned database migrations |
| **OpenFeign** | External anagram provider integration |
| **MapStruct** | Explicit, low-boilerplate mapping between layers |
| **Bean Validation** | Contract validation at the API boundary |
| **OpenAPI + Scalar** | Documentation that also works as a live playground |
| **Docker Compose** | Local orchestration for the API and PostgreSQL |
| **Automated tests** | Coverage across domain, application, web, and docs |

## Getting Started

Current local entry point:

```text
http://localhost:8080
```

## Technical Docs

| Document | URL |
| --- | --- |
| **Scalar playground** | `http://localhost:8080/scalar` |
| **OpenAPI spec** | `http://localhost:8080/openapi.yaml` |

### Option 1: Play in Scalar

Open the playground:

```text
http://localhost:8080/scalar
```

Quick flow:

1. Call `GET /api/anagrams/daily` to reveal the board.
2. Send guesses to `POST /api/anagrams/daily/answers`.
3. Optional: create a player with `POST /api/player-session`.
4. Keep submitting guesses and watch the board fill up.

Important:
the daily board is global, so every revealed answer becomes visible to everyone playing that same challenge.

### Option 2: Play in the terminal

The API also supports a terminal-friendly `text/plain` flow.

#### 1. See the board

```bash
curl -H "Accept: text/plain" http://localhost:8080/api/anagrams/daily
```

#### 2. Submit a guess

```bash
curl -X POST http://localhost:8080/api/anagrams/daily/answers \
  -H "Content-Type: application/json" \
  -H "Accept: text/plain" \
  -d '{"answer":"your_guess"}'
```

#### 3. Claim a player tag

```bash
curl -c .anagram-cookie \
  -X POST http://localhost:8080/api/player-session \
  -H "Content-Type: application/json" \
  -d '{"name":"your_name"}'
```

#### 4. Play with the same session

```bash
curl -b .anagram-cookie -c .anagram-cookie \
  -X POST http://localhost:8080/api/anagrams/daily/answers \
  -H "Content-Type: application/json" \
  -H "Accept: text/plain" \
  -d '{"answer":"your_guess"}'
```

#### 5. Leave the session

```bash
curl -X DELETE -b .anagram-cookie http://localhost:8080/api/player-session
```

If you are using PowerShell, prefer `curl.exe` instead of `curl`.

## Architecture Snapshot

```text
web -> use cases -> domain -> ports -> persistence/http adapters
```
