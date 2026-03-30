package murillo.tavares.anagram_api.adapter.out.http.mapper;

import murillo.tavares.anagram_api.adapter.out.http.dto.FreeApiAnagramResponse;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FreeApiAnagramMapperTest {

	private final FreeApiAnagramMapper mapper = new FreeApiAnagramMapperImpl();

	@Test
	void shouldShuffleTaskLettersAndIncludeOriginalWordAsSolution() {
		Anagram anagram = mapper.toDomain(new FreeApiAnagramResponse(
				"psychology",
				List.of("soy", "spy", "copy")
		));

		assertThat(anagram.letters()).isEqualTo("scooypyhlg");
		assertThat(anagram.solutions()).containsExactly("soy", "spy", "copy", "psychology");
	}
}
