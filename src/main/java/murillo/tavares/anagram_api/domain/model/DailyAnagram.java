package murillo.tavares.anagram_api.domain.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
		this(date, anagram.letters(), toSolutions(anagram, List.of()));
	}

	public DailyAnagram(LocalDate date, Anagram anagram, List<String> foundSolutions) {
		this(date, anagram.letters(), toSolutions(anagram, foundSolutions));
	}

	public boolean isNewValidSolution(String answer) {
		return solutions.stream()
				.anyMatch(solution -> solution.isNewValidSolution(answer));
	}

	private static List<DailyAnagramSolution> toSolutions(Anagram anagram, List<String> foundSolutions) {
		Set<String> foundAnswers = toFoundAnswers(foundSolutions);
		return anagram.solutions().stream()
				.map(solution -> toSolution(solution, foundAnswers))
				.toList();
	}

	private static Set<String> toFoundAnswers(List<String> foundSolutions) {
		return foundSolutions.stream()
				.map(solution -> new DailyAnagramSolution(solution, true).answer())
				.collect(Collectors.toSet());
	}

	private static DailyAnagramSolution toSolution(String answer, Set<String> foundAnswers) {
		return new DailyAnagramSolution(answer, foundAnswers.contains(answer));
	}
}
