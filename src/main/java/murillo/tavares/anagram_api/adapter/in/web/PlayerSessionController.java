package murillo.tavares.anagram_api.adapter.in.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.adapter.in.web.mapper.PlayerSessionWebMapper;
import murillo.tavares.anagram_api.adapter.in.web.request.CreatePlayerSessionRequest;
import murillo.tavares.anagram_api.adapter.in.web.response.PlayerSessionResponse;
import murillo.tavares.anagram_api.adapter.in.web.session.PlayerSessionManager;
import murillo.tavares.anagram_api.domain.model.PlayerIdentity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player-session")
@RequiredArgsConstructor
public class PlayerSessionController {

	private final PlayerSessionManager playerSessionManager;
	private final PlayerSessionWebMapper playerSessionWebMapper;

	@GetMapping
	public PlayerSessionResponse getCurrentPlayer(HttpSession session) {
		return playerSessionManager.getCurrentPlayer(session)
				.map(playerSessionWebMapper::toResponse)
				.orElseGet(playerSessionWebMapper::anonymousResponse);
	}

	@PostMapping
	public PlayerSessionResponse createOrUpdatePlayer(
			@Valid @RequestBody CreatePlayerSessionRequest request,
			HttpSession session
	) {
		PlayerIdentity playerIdentity = playerSessionManager.createOrUpdate(session, request.name());
		return playerSessionWebMapper.toResponse(playerIdentity);
	}

	@DeleteMapping
	public ResponseEntity<Void> clearPlayerSession(HttpSession session) {
		playerSessionManager.clear(session);
		return ResponseEntity.noContent().build();
	}
}
