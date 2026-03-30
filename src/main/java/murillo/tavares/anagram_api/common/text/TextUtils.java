package murillo.tavares.anagram_api.common.text;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public final class TextUtils {

	private TextUtils() {
	}

	public static String normalize(String value) {
		return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
	}

	public static List<String> normalizeDistinct(Collection<String> values) {
		return values.stream()
				.map(TextUtils::normalize)
				.distinct()
				.toList();
	}

	public static String shuffleLetters(String value) {
		if (value.length() < 2) {
			return value;
		}

		StringBuilder oddIndexCharacters = new StringBuilder();
		StringBuilder evenIndexCharacters = new StringBuilder();

		for (int index = 0; index < value.length(); index++) {
			if (index % 2 == 0) {
				evenIndexCharacters.append(value.charAt(index));
				continue;
			}

			oddIndexCharacters.append(value.charAt(index));
		}

		String shuffledValue = oddIndexCharacters.append(evenIndexCharacters).toString();
		return shuffledValue.equals(value) ? rotateLeft(value) : shuffledValue;
	}

	private static String rotateLeft(String value) {
		return value.substring(1) + value.charAt(0);
	}
}
