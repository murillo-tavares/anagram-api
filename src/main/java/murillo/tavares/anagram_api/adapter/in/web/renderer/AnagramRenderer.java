package murillo.tavares.anagram_api.adapter.in.web.renderer;

import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.springframework.http.MediaType;

public interface AnagramRenderer {

	MediaType contentType();

	Object renderBody(DailyAnagram dailyAnagram);

	default boolean supports(MediaType mediaType) {
		return mediaType.isCompatibleWith(contentType());
	}
}
