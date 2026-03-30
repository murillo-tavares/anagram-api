package murillo.tavares.anagram_api.domain.model;

import murillo.tavares.anagram_api.common.text.TextUtils;

import java.time.LocalDate;
import java.util.List;

public record DailyAnagram(
		LocalDate date,
		Anagram anagram,
		List<String> foundSolutions
) {

	public DailyAnagram {
		foundSolutions = TextUtils.normalizeDistinct(foundSolutions);
	}

	public DailyAnagram(LocalDate date, Anagram anagram) {
		this(date, anagram, List.of());
	}

	public boolean isNewValidSolution(String answer) {
		return anagram.isValidAnswer(answer) && !hasFoundAnswer(answer);
	}

	public boolean hasFoundAnswer(String answer) {
		return foundSolutions.contains(TextUtils.normalize(answer));
	}
}
