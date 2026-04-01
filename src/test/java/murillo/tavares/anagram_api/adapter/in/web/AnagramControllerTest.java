package murillo.tavares.anagram_api.adapter.in.web;

import murillo.tavares.anagram_api.adapter.in.web.error.ApiExceptionHandler;
import murillo.tavares.anagram_api.adapter.in.web.mapper.AnagramWebMapperImpl;
import murillo.tavares.anagram_api.adapter.in.web.session.PlayerSessionManager;
import murillo.tavares.anagram_api.adapter.out.http.exception.AnagramProviderException;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.application.port.in.SubmitDailyAnagramAnswerUseCase;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagramSolution;
import murillo.tavares.anagram_api.domain.model.PlayerIdentity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnagramController.class)
@Import({AnagramControllerTest.MockConfig.class, ApiExceptionHandler.class, AnagramWebMapperImpl.class})
class AnagramControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GetDailyAnagramUseCase getDailyAnagramUseCase;

	@Autowired
	private SubmitDailyAnagramAnswerUseCase submitDailyAnagramAnswerUseCase;

	@Autowired
	private PlayerSessionManager playerSessionManager;

	@BeforeEach
	void resetMock() {
		Mockito.reset(getDailyAnagramUseCase);
		Mockito.reset(submitDailyAnagramAnswerUseCase);
		Mockito.reset(playerSessionManager);
	}

	@Test
	void shouldReturnDailyAnagram() throws Exception {
		when(getDailyAnagramUseCase.getDailyAnagram()).thenReturn(new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, "ana#0001"),
						new DailyAnagramSolution("spy", false, null),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		));

		mockMvc.perform(get("/api/anagrams/daily"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.letters").value("scooypyhlg"))
				.andExpect(jsonPath("$.solutions[0].value").value("soy"))
				.andExpect(jsonPath("$.solutions[0].foundBy").value("ana#0001"))
				.andExpect(jsonPath("$.solutions[1].value").value("---"))
				.andExpect(jsonPath("$.solutions[1].foundBy").value(nullValue()))
				.andExpect(jsonPath("$.solutions[2].value").value("----"))
				.andExpect(jsonPath("$.solutions[2].foundBy").value(nullValue()))
				.andExpect(jsonPath("$.solutions[3].value").value("----------"))
				.andExpect(jsonPath("$.solutions[3].foundBy").value(nullValue()));
	}

	@Test
	void shouldSubmitValidAnswerWithPlayerSession() throws Exception {
		MockHttpSession session = new MockHttpSession();
		when(playerSessionManager.getCurrentPlayer(session)).thenReturn(Optional.of(new PlayerIdentity("murillo", "0001")));
		when(submitDailyAnagramAnswerUseCase.submitAnswer("spy", "murillo#0001")).thenReturn(new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				"scooypyhlg",
				List.of(
						new DailyAnagramSolution("soy", true, "ana#0001"),
						new DailyAnagramSolution("spy", true, "murillo#0001"),
						new DailyAnagramSolution("copy", false, null),
						new DailyAnagramSolution("psychology", false, null)
				)
		));

		mockMvc.perform(post("/api/anagrams/daily/answers")
						.session(session)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "answer": "spy"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.letters").value("scooypyhlg"))
				.andExpect(jsonPath("$.solutions[0].value").value("soy"))
				.andExpect(jsonPath("$.solutions[0].foundBy").value("ana#0001"))
				.andExpect(jsonPath("$.solutions[1].value").value("spy"))
				.andExpect(jsonPath("$.solutions[1].foundBy").value("murillo#0001"))
				.andExpect(jsonPath("$.solutions[2].value").value("----"))
				.andExpect(jsonPath("$.solutions[2].foundBy").value(nullValue()))
				.andExpect(jsonPath("$.solutions[3].value").value("----------"))
				.andExpect(jsonPath("$.solutions[3].foundBy").value(nullValue()));
	}

	@Test
	void shouldReturnCurrentStateWhenAnswerDoesNotChangePuzzle() throws Exception {
		when(submitDailyAnagramAnswerUseCase.submitAnswer("invalid", null))
				.thenReturn(new DailyAnagram(
						LocalDate.of(2026, 3, 27),
						"scooypyhlg",
						List.of(
								new DailyAnagramSolution("soy", true, "ana#0001"),
								new DailyAnagramSolution("spy", false, null),
								new DailyAnagramSolution("copy", false, null),
								new DailyAnagramSolution("psychology", false, null)
						)
				));

		mockMvc.perform(post("/api/anagrams/daily/answers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "answer": "invalid"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.letters").value("scooypyhlg"))
				.andExpect(jsonPath("$.solutions[0].value").value("soy"))
				.andExpect(jsonPath("$.solutions[0].foundBy").value("ana#0001"))
				.andExpect(jsonPath("$.solutions[1].value").value("---"))
				.andExpect(jsonPath("$.solutions[1].foundBy").value(nullValue()))
				.andExpect(jsonPath("$.solutions[2].value").value("----"))
				.andExpect(jsonPath("$.solutions[2].foundBy").value(nullValue()))
				.andExpect(jsonPath("$.solutions[3].value").value("----------"))
				.andExpect(jsonPath("$.solutions[3].foundBy").value(nullValue()));
	}

	@Test
	void shouldReturnBadGatewayWhenProviderFails() throws Exception {
		when(getDailyAnagramUseCase.getDailyAnagram())
				.thenThrow(new AnagramProviderException("Failed to fetch anagram from FreeAPI provider"));

		mockMvc.perform(get("/api/anagrams/daily").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadGateway())
				.andExpect(jsonPath("$.title").value("External anagram provider failure"))
				.andExpect(jsonPath("$.detail").value("Failed to fetch anagram from FreeAPI provider"));
	}

	@TestConfiguration
	static class MockConfig {

		@Bean
		GetDailyAnagramUseCase getDailyAnagramUseCase() {
			return Mockito.mock(GetDailyAnagramUseCase.class);
		}

		@Bean
		SubmitDailyAnagramAnswerUseCase submitDailyAnagramAnswerUseCase() {
			return Mockito.mock(SubmitDailyAnagramAnswerUseCase.class);
		}

		@Bean
		PlayerSessionManager playerSessionManager() {
			return Mockito.mock(PlayerSessionManager.class);
		}
	}
}
