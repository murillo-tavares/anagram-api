package murillo.tavares.anagram_api.domain.model;

import murillo.tavares.anagram_api.common.text.TextUtils;

public record PlayerIdentity(
		String name,
		String discriminator
) {

	public static final int MIN_NAME_LENGTH = 3;
	public static final int MAX_NAME_LENGTH = 20;
	public static final String NAME_PATTERN = "^[A-Za-z0-9_]+$";

	public PlayerIdentity {
		name = normalizeName(name);
		discriminator = normalizeDiscriminator(discriminator);
	}

	public String playerTag() {
		return name + "#" + discriminator;
	}

	public boolean hasName(String value) {
		return name.equals(normalizeName(value));
	}

	public static String normalizeName(String name) {
		return TextUtils.normalize(name);
	}

	public static String formatDiscriminator(int value) {
		return "%04d".formatted(value);
	}

	private static String normalizeDiscriminator(String discriminator) {
		return discriminator == null ? "" : discriminator.trim();
	}
}
