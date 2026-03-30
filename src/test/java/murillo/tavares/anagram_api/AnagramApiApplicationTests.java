package murillo.tavares.anagram_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"freeapi-anagram-provider.base-url=http://localhost",
		"spring.datasource.url=jdbc:h2:mem:anagram-api-test;DB_CLOSE_DELAY=-1"
})
class AnagramApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
