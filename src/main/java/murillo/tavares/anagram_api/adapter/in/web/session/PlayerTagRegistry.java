package murillo.tavares.anagram_api.adapter.in.web.session;

import org.springframework.stereotype.Component;

import murillo.tavares.anagram_api.domain.model.PlayerIdentity;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class PlayerTagRegistry {

	private static final int DISCRIMINATOR_SPACE = 10_000;
	private static final int MAX_GENERATION_ATTEMPTS = 10_000;

	private final Map<String, String> playerTagsBySessionId = new HashMap<>();
	private final Set<String> activePlayerTags = new HashSet<>();
	private final SecureRandom secureRandom = new SecureRandom();

	public synchronized boolean isAvailable(String playerTag) {
		return !activePlayerTags.contains(playerTag);
	}

	public synchronized PlayerIdentity registerNewPlayerIdentity(String sessionId, String requestedName) {
		for (int attempt = 0; attempt < MAX_GENERATION_ATTEMPTS; attempt++) {
			int discriminatorValue = secureRandom.nextInt(DISCRIMINATOR_SPACE);
			String discriminator = PlayerIdentity.formatDiscriminator(discriminatorValue);
			PlayerIdentity playerIdentity = new PlayerIdentity(requestedName, discriminator);

			if (isAvailable(playerIdentity.playerTag())) {
				register(sessionId, playerIdentity.playerTag());
				return playerIdentity;
			}
		}

		throw new IllegalStateException("Could not allocate a unique discriminator for player name");
	}

	public synchronized void register(String sessionId, String playerTag) {
		String previousPlayerTag = playerTagsBySessionId.put(sessionId, playerTag);

		if (previousPlayerTag != null) {
			activePlayerTags.remove(previousPlayerTag);
		}

		activePlayerTags.add(playerTag);
	}

	public synchronized void releaseBySessionId(String sessionId) {
		String playerTag = playerTagsBySessionId.remove(sessionId);

		if (playerTag != null) {
			activePlayerTags.remove(playerTag);
		}
	}
}
