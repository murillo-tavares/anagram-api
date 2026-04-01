package murillo.tavares.anagram_api.adapter.in.web.renderer;

import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.adapter.in.web.mapper.AnagramWebMapper;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonAnagramRenderer implements AnagramRenderer {

	private final AnagramWebMapper anagramWebMapper;

	@Override
	public MediaType contentType() {
		return MediaType.APPLICATION_JSON;
	}

	@Override
	public Object renderBody(DailyAnagram dailyAnagram) {
		return anagramWebMapper.toResponse(dailyAnagram);
	}
}
