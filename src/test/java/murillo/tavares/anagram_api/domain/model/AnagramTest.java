package murillo.tavares.anagram_api.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnagramTest {

	@Test
	void shouldNormalizeSolutionsAndValidateAnswers() {
		Anagram anagram = new Anagram("psychology", List.of(" Soy ", "SPY", "spy", "copy"));

		assertThat(anagram.solutions()).containsExactly("soy", "spy", "copy");
	}

	@Test
	void shouldIdentifyWhenAnswerIsNewValidSolutionForDailyAnagram() {
		DailyAnagram dailyAnagram = new DailyAnagram(
				java.time.LocalDate.of(2026, 3, 27),
				new Anagram("psychology", List.of("soy", "spy", "copy")),
				List.of("soy")
		);

		assertThat(dailyAnagram.isNewValidSolution("spy")).isTrue();
		assertThat(dailyAnagram.isNewValidSolution("soy")).isFalse();
		assertThat(dailyAnagram.isNewValidSolution("invalid")).isFalse();
		assertThat(dailyAnagram.isNewValidSolution(" Spy ")).isTrue();
	}

	@Test
	void shouldOrderDailyAnagramSolutionsByLengthAndAlphabetically() {
		DailyAnagram dailyAnagram = new DailyAnagram(
				java.time.LocalDate.of(2026, 3, 27),
				"psychology",
				List.of(
						new DailyAnagramSolution("longer", false),
						new DailyAnagramSolution("copy", false),
						new DailyAnagramSolution("spy", false),
						new DailyAnagramSolution("soy", false)
				)
		);

		assertThat(dailyAnagram.solutions())
				.extracting(DailyAnagramSolution::answer)
				.containsExactly("soy", "spy", "copy", "longer");
	}
}
