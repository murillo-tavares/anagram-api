package murillo.tavares.anagram_api.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnagramTest {

	@Test
	void shouldNormalizeLettersAndSolutions() {
		Anagram anagram = new Anagram(" Scooypyhlg ", List.of(" Soy ", "SPY", "spy", "copy", "psychology"));

		assertThat(anagram.letters()).isEqualTo("scooypyhlg");
		assertThat(anagram.solutions()).containsExactly("soy", "spy", "copy", "psychology");
	}

	@Test
	void shouldIdentifyWhenAnswerIsNewValidSolutionForDailyAnagram() {
		DailyAnagram dailyAnagram = new DailyAnagram(
				java.time.LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, null),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		);

		assertThat(dailyAnagram.isNewValidSolution("spy")).isTrue();
		assertThat(dailyAnagram.isNewValidSolution("soy")).isFalse();
		assertThat(dailyAnagram.isNewValidSolution("invalid")).isFalse();
		assertThat(dailyAnagram.isNewValidSolution(" Spy ")).isTrue();
		assertThat(dailyAnagram.isNewValidSolution("psychology")).isTrue();
	}

	@Test
	void shouldOrderDailyAnagramSolutionsByLengthAndAlphabetically() {
		DailyAnagram dailyAnagram = new DailyAnagram(
				java.time.LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("longer", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("soy", false, null)
				)
		);

		assertThat(dailyAnagram.solutions())
				.extracting(DailyAnagramSolution::answer)
				.containsExactly("soy", "spy", "copy", "longer");
	}
}
