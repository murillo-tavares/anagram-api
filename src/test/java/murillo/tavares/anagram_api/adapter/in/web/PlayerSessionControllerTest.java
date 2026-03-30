package murillo.tavares.anagram_api.adapter.in.web;

import com.jayway.jsonpath.JsonPath;
import murillo.tavares.anagram_api.adapter.in.web.mapper.PlayerSessionWebMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerSessionController.class)
@Import({
		PlayerSessionController.class,
		PlayerSessionWebMapperImpl.class,
		murillo.tavares.anagram_api.adapter.in.web.error.ApiExceptionHandler.class,
		murillo.tavares.anagram_api.adapter.in.web.session.PlayerSessionManager.class,
		murillo.tavares.anagram_api.adapter.in.web.session.PlayerTagRegistry.class
})
class PlayerSessionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnAnonymousWhenSessionHasNoPlayer() throws Exception {
		mockMvc.perform(get("/api/player-session"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identified").value(false))
				.andExpect(jsonPath("$.playerTag").doesNotExist());
	}

	@Test
	void shouldCreateSessionWithPlayerHandle() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/player-session")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "name": "Joao"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identified").value(true))
				.andExpect(jsonPath("$.playerTag").value(org.hamcrest.Matchers.matchesPattern("joao#\\d{4}")))
				.andReturn();

		MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
		String createdPlayerTag = JsonPath.read(result.getResponse().getContentAsString(), "$.playerTag");

		mockMvc.perform(get("/api/player-session").session(session))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identified").value(true))
				.andExpect(jsonPath("$.playerTag").value(createdPlayerTag));
	}

	@Test
	void shouldGenerateDifferentHandlesForDifferentSessionsWithSameName() throws Exception {
		MvcResult firstResult = mockMvc.perform(post("/api/player-session")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "name": "joao"
								}
								"""))
				.andExpect(status().isOk())
				.andReturn();

		MvcResult secondResult = mockMvc.perform(post("/api/player-session")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "name": "joao"
								}
								"""))
				.andExpect(status().isOk())
				.andReturn();

		String firstPlayerTag = JsonPath.read(firstResult.getResponse().getContentAsString(), "$.playerTag");
		String secondPlayerTag = JsonPath.read(secondResult.getResponse().getContentAsString(), "$.playerTag");

		assertThat(firstPlayerTag).startsWith("joao#");
		assertThat(secondPlayerTag).startsWith("joao#");
		assertThat(firstPlayerTag).isNotEqualTo(secondPlayerTag);
	}

	@Test
	void shouldRejectInvalidPlayerName() throws Exception {
		mockMvc.perform(post("/api/player-session")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "name": "joao!"
								}
								"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldClearPlayerSession() throws Exception {
		MvcResult result = mockMvc.perform(post("/api/player-session")
						.contentType(APPLICATION_JSON)
						.content("""
								{
								  "name": "joao"
								}
								"""))
				.andExpect(status().isOk())
				.andReturn();

		MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);

		mockMvc.perform(delete("/api/player-session").session(session))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/player-session"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identified").value(false));
	}
}
