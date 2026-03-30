package murillo.tavares.anagram_api.domain.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record DailyAnagram(
		LocalDate date,
		String task,
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
		this(date, anagram.task(), toSolutions(anagram, List.of()));
	}

	public DailyAnagram(LocalDate date, Anagram anagram, List<String> foundSolutions) {
		this(date, anagram.task(), toSolutions(anagram, foundSolutions));
	}

	public boolean isNewValidSolution(String answer) {
		return solutions.stream()
				.anyMatch(solution -> solution.isNewValidSolution(answer));
	}

	private static List<DailyAnagramSolution> toSolutions(Anagram anagram, List<String> foundSolutions) {
		List<String> normalizedFoundSolutions = foundSolutions.stream()
				.map(DailyAnagramSolution::normalizeAnswer)
				.distinct()
				.toList();
		return anagram.solutions().stream()
				.map(solution -> new DailyAnagramSolution(solution, normalizedFoundSolutions.contains(solution)))
				.toList();
	}
}
