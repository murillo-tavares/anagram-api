package murillo.tavares.anagram_api.adapter.in.web.session;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.domain.model.PlayerIdentity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerSessionManager {

	private static final String PLAYER_IDENTITY_ATTRIBUTE = "playerIdentity";

	private final PlayerTagRegistry playerTagRegistry;

	public Optional<PlayerIdentity> getCurrentPlayer(HttpSession session) {
		return Optional.ofNullable((PlayerIdentity) session.getAttribute(PLAYER_IDENTITY_ATTRIBUTE));
	}

	public PlayerIdentity createOrUpdate(HttpSession session, String requestedName) {
		return getCurrentPlayer(session)
				.filter(currentPlayer -> currentPlayer.hasName(requestedName))
				.orElseGet(() -> createPlayer(session, requestedName));
	}

	public void clear(HttpSession session) {
		playerTagRegistry.releaseBySessionId(session.getId());
		session.invalidate();
	}

	private PlayerIdentity createPlayer(HttpSession session, String requestedName) {
		PlayerIdentity playerIdentity = playerTagRegistry.registerNewPlayerIdentity(session.getId(), requestedName);
		session.setAttribute(PLAYER_IDENTITY_ATTRIBUTE, playerIdentity);
		return playerIdentity;
	}
}
