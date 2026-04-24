package murillo.tavares.anagram_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
		"freeapi-anagram-provider.base-url=http://localhost",
		"spring.datasource.url=jdbc:h2:mem:anagram-api-docs-test;DB_CLOSE_DELAY=-1",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"scalar.url=/openapi.yaml"
})
@AutoConfigureMockMvc
class ApiDocumentationIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldExposeOpenApiDescription() throws Exception {
		mockMvc.perform(get("/openapi.yaml"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("title: Anagram Arena API")))
				.andExpect(content().string(containsString("version: v1")))
				.andExpect(content().string(containsString("/api/anagrams/daily:")))
				.andExpect(content().string(containsString("/api/player-session:")))
				.andExpect(content().string(containsString("text/plain:")))
				.andExpect(content().string(containsString("application/problem+json:")));
	}

	@Test
	void shouldExposeScalarPlayground() throws Exception {
		mockMvc.perform(get("/scalar").accept(TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
				.andExpect(content().string(containsString("/openapi.yaml")))
				.andExpect(content().string(containsString("Scalar")));
	}
}
