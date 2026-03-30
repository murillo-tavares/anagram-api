package murillo.tavares.anagram_api.adapter.in.web;

import murillo.tavares.anagram_api.adapter.in.web.error.ApiExceptionHandler;
import murillo.tavares.anagram_api.adapter.in.web.mapper.AnagramWebMapperImpl;
import murillo.tavares.anagram_api.adapter.out.http.exception.AnagramProviderException;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.application.port.in.SubmitDailyAnagramAnswerUseCase;
import murillo.tavares.anagram_api.domain.model.Anagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
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

import java.time.LocalDate;
import java.util.List;

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

	@BeforeEach
	void resetMock() {
		Mockito.reset(getDailyAnagramUseCase);
		Mockito.reset(submitDailyAnagramAnswerUseCase);
	}

	@Test
	void shouldReturnDailyAnagram() throws Exception {
		when(getDailyAnagramUseCase.getDailyAnagram()).thenReturn(new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				new Anagram("psychology", List.of("soy", "spy", "copy", "polo")),
				List.of("soy")
		));

		mockMvc.perform(get("/api/anagrams/daily"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.task").value("psychology"))
				.andExpect(jsonPath("$.totalSolutions").value(4))
				.andExpect(jsonPath("$.foundSolutions[0]").value("soy"));
	}

	@Test
	void shouldSubmitValidAnswer() throws Exception {
		when(submitDailyAnagramAnswerUseCase.submitAnswer("spy")).thenReturn(new DailyAnagram(
				LocalDate.of(2026, 3, 27),
				new Anagram("psychology", List.of("soy", "spy", "copy", "polo")),
				List.of("soy", "spy")
		));

		mockMvc.perform(post("/api/anagrams/daily/answers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "answer": "spy"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.task").value("psychology"))
				.andExpect(jsonPath("$.totalSolutions").value(4))
				.andExpect(jsonPath("$.foundSolutions[1]").value("spy"));
	}

	@Test
	void shouldReturnCurrentStateWhenAnswerDoesNotChangePuzzle() throws Exception {
		when(submitDailyAnagramAnswerUseCase.submitAnswer("invalid"))
				.thenReturn(new DailyAnagram(
						LocalDate.of(2026, 3, 27),
						new Anagram("psychology", List.of("soy", "spy", "copy", "polo")),
						List.of("soy")
				));

		mockMvc.perform(post("/api/anagrams/daily/answers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "answer": "invalid"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.task").value("psychology"))
				.andExpect(jsonPath("$.totalSolutions").value(4))
				.andExpect(jsonPath("$.foundSolutions[0]").value("soy"));
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
	}
}
