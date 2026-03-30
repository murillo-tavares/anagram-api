package murillo.tavares.anagram_api.domain.model;

import murillo.tavares.anagram_api.common.text.TextUtils;

import java.util.List;

public record Anagram(
		String letters,
		List<String> solutions
) {

	public Anagram {
		letters = TextUtils.normalize(letters);
		solutions = TextUtils.normalizeDistinct(solutions);
	}
}
