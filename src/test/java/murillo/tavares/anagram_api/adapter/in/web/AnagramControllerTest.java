package murillo.tavares.anagram_api.adapter.in.web;

import murillo.tavares.anagram_api.adapter.in.web.error.ApiExceptionHandler;
import murillo.tavares.anagram_api.adapter.out.http.exception.AnagramProviderException;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.domain.model.Anagram;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnagramController.class)
@Import({AnagramControllerTest.MockConfig.class, ApiExceptionHandler.class})
class AnagramControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private GetDailyAnagramUseCase getDailyAnagramUseCase;

	@BeforeEach
	void resetMock() {
		Mockito.reset(getDailyAnagramUseCase);
	}

	@Test
	void shouldReturnDailyAnagram() throws Exception {
		when(getDailyAnagramUseCase.getDailyAnagram()).thenReturn(new Anagram(
				"psychology",
				List.of("soy", "spy", "copy", "polo")
		));

		mockMvc.perform(get("/api/anagrams/daily"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.task").value("psychology"))
				.andExpect(jsonPath("$.solutions[0]").value("soy"));
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
	}
}
