package murillo.tavares.anagram_api.domain.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record DailyAnagram(
		LocalDate date,
		String letters,
		List<DailyAnagramSolution> solutions
) {

	public DailyAnagram {
		solutions = solutions.stream()
				.sorted(
						Comparator.comparingInt((DailyAnagramSolution solution) -> solution.answer().length())
								.thenComparing(DailyAnagramSolution::answer)
				)
				.toList();
	}

	public DailyAnagram(LocalDate date, Anagram anagram) {
		this(date, anagram.letters(), anagram.solutions().stream()
				.map(solution -> new DailyAnagramSolution(solution, false, null))
				.toList());
	}

	public boolean isNewValidSolution(String answer) {
		return solutions.stream()
				.anyMatch(solution -> solution.isNewValidSolution(answer));
	}
}
