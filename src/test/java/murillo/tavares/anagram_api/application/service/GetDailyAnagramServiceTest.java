package murillo.tavares.anagram_api.application.service;

import murillo.tavares.anagram_api.application.port.out.GenerateAnagramPort;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetDailyAnagramServiceTest {

	@Test
	void shouldReturnDailyAnagramFromOutputPort() {
		GenerateAnagramPort generateAnagramPort = mock(GenerateAnagramPort.class);
		GetDailyAnagramService service = new GetDailyAnagramService(generateAnagramPort);
		Anagram expected = new Anagram(
				"psychology",
				List.of("soy", "spy", "copy")
		);

		when(generateAnagramPort.generateAnagram()).thenReturn(expected);

		Anagram actual = service.getDailyAnagram();

		assertThat(actual).isEqualTo(expected);
		verify(generateAnagramPort).generateAnagram();
	}
}
