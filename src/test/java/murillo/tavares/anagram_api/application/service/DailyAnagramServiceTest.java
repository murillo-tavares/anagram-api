package murillo.tavares.anagram_api.application.service;

import murillo.tavares.anagram_api.application.port.out.GenerateAnagramPort;
import murillo.tavares.anagram_api.application.port.out.ManageDailyAnagramPort;
import murillo.tavares.anagram_api.common.date.DateUtils;
import murillo.tavares.anagram_api.domain.model.Anagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagramSolution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class DailyAnagramServiceTest {

	@BeforeEach
	void setUp() {
		DateUtils.setClock(fixedClock());
	}

	@AfterEach
	void tearDown() {
		DateUtils.resetClock();
	}

	@Test
	void shouldReturnPersistedDailyAnagramWhenItAlreadyExists() {
		GenerateAnagramPort generateAnagramPort = mock(GenerateAnagramPort.class);
		ManageDailyAnagramPort manageDailyAnagramPort = mock(ManageDailyAnagramPort.class);
		DailyAnagramService service = new DailyAnagramService(generateAnagramPort, manageDailyAnagramPort);
		DailyAnagram expected = new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, null),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		);

		when(manageDailyAnagramPort.findByDate(LocalDate.of(2026, 3, 27))).thenReturn(Optional.of(expected));

		DailyAnagram actual = service.getDailyAnagram();

		assertThat(actual).isEqualTo(expected);
		verify(manageDailyAnagramPort).findByDate(LocalDate.of(2026, 3, 27));
		verifyNoInteractions(generateAnagramPort);
	}

	@Test
	void shouldGenerateAndPersistDailyAnagramWhenNoneExists() {
		GenerateAnagramPort generateAnagramPort = mock(GenerateAnagramPort.class);
		ManageDailyAnagramPort manageDailyAnagramPort = mock(ManageDailyAnagramPort.class);
		DailyAnagramService service = new DailyAnagramService(generateAnagramPort, manageDailyAnagramPort);
		Anagram generatedAnagram = new Anagram("scooypyhlg", List.of("soy", "spy", "copy", "psychology"));
		DailyAnagram persistedAnagram = new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", false, null),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		);

		when(manageDailyAnagramPort.findByDate(LocalDate.of(2026, 3, 27))).thenReturn(Optional.empty());
		when(generateAnagramPort.generateAnagram()).thenReturn(generatedAnagram);
		when(manageDailyAnagramPort.save(new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				new Anagram("scooypyhlg", List.of("soy", "spy", "copy", "psychology"))
		))).thenReturn(persistedAnagram);

		DailyAnagram actual = service.getDailyAnagram();

		assertThat(actual).isEqualTo(persistedAnagram);
		verify(generateAnagramPort).generateAnagram();
		verify(manageDailyAnagramPort).save(new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				new Anagram("scooypyhlg", List.of("soy", "spy", "copy", "psychology"))
		));
	}

	@Test
	void shouldMarkValidAnswerAsFound() {
		GenerateAnagramPort generateAnagramPort = mock(GenerateAnagramPort.class);
		ManageDailyAnagramPort manageDailyAnagramPort = mock(ManageDailyAnagramPort.class);
		DailyAnagramService service = new DailyAnagramService(generateAnagramPort, manageDailyAnagramPort);
		DailyAnagram existingAnagram = new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, null),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		);
		DailyAnagram updatedAnagram = new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, null),
						new DailyAnagramSolution("spy", true, "murillo#0001"),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		);

		when(manageDailyAnagramPort.findByDate(LocalDate.of(2026, 3, 27)))
				.thenReturn(Optional.of(existingAnagram));
		when(manageDailyAnagramPort.markSolutionAsFound(existingAnagram, " Spy ", "murillo#0001"))
				.thenReturn(updatedAnagram);

		DailyAnagram actual = service.submitAnswer(" Spy ", "murillo#0001");

		assertThat(actual).isEqualTo(updatedAnagram);
		verify(manageDailyAnagramPort).markSolutionAsFound(existingAnagram, " Spy ", "murillo#0001");
	}

	@Test
	void shouldReturnCurrentStateWhenAnswerIsInvalid() {
		GenerateAnagramPort generateAnagramPort = mock(GenerateAnagramPort.class);
		ManageDailyAnagramPort manageDailyAnagramPort = mock(ManageDailyAnagramPort.class);
		DailyAnagramService service = new DailyAnagramService(generateAnagramPort, manageDailyAnagramPort);
		DailyAnagram existingAnagram = new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, null),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		);

		when(manageDailyAnagramPort.findByDate(LocalDate.of(2026, 3, 27))).thenReturn(Optional.of(existingAnagram));

		DailyAnagram actual = service.submitAnswer("invalid", null);

		assertThat(actual).isEqualTo(existingAnagram);
		verify(manageDailyAnagramPort).findByDate(LocalDate.of(2026, 3, 27));
		verify(manageDailyAnagramPort, never()).markSolutionAsFound(existingAnagram, "invalid", null);
	}

	private Clock fixedClock() {
		return Clock.fixed(Instant.parse("2026-03-27T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
	}
}
