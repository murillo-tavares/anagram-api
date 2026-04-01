package murillo.tavares.anagram_api.domain.model;

import murillo.tavares.anagram_api.common.text.TextUtils;

public record DailyAnagramSolution(
		String answer,
		boolean found,
		String foundBy
) {

	public DailyAnagramSolution {
		answer = normalizeAnswer(answer);
		foundBy = foundBy != null ? TextUtils.normalize(foundBy) : null;
	}

	public boolean matches(String value) {
		return answer.equals(normalizeAnswer(value));
	}

	public boolean isNewValidSolution(String value) {
		return matches(value) && !found;
	}

	public String formattedValue() {
		return found ? answer : "-".repeat(answer.length());
	}

	public static String normalizeAnswer(String answer) {
		return TextUtils.normalize(answer);
	}
}
