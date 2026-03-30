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
}
