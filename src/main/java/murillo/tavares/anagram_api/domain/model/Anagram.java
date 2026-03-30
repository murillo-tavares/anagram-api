package murillo.tavares.anagram_api.domain.model;

import murillo.tavares.anagram_api.common.text.TextUtils;

import java.util.List;

public record Anagram(
		String task,
		List<String> solutions
) {

	public Anagram {
		solutions = TextUtils.normalizeDistinct(solutions);
	}

	public int totalSolutionsCount() {
		return solutions.size();
	}

	public boolean isValidAnswer(String answer) {
		return solutions.contains(TextUtils.normalize(answer));
	}
}
