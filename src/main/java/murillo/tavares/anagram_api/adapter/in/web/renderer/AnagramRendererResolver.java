package murillo.tavares.anagram_api.adapter.in.web.renderer;

import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnagramRendererResolver {

	private final List<AnagramRenderer> renderers;

	public AnagramRenderer resolveRenderer(String acceptHeader) {
		for (MediaType mediaType : requestedMediaTypes(acceptHeader)) {
			if (mediaType.isWildcardType()) {
				continue;
			}

			for (AnagramRenderer renderer : renderers) {
				if (renderer.supports(mediaType)) {
					return renderer;
				}
			}
		}

		return defaultRenderer();
	}

	public ResponseEntity<?> resolveResponseEntity(String acceptHeader, DailyAnagram dailyAnagram) {
		AnagramRenderer renderer = resolveRenderer(acceptHeader);
		return ResponseEntity.ok()
				.contentType(renderer.contentType())
				.body(renderer.renderBody(dailyAnagram));
	}

	private List<MediaType> requestedMediaTypes(String acceptHeader) {
		if (acceptHeader == null || acceptHeader.isBlank()) {
			return List.of();
		}

		List<MediaType> mediaTypes = new ArrayList<>(MediaType.parseMediaTypes(acceptHeader));
		mediaTypes.sort(
				Comparator.comparingDouble(MediaType::getQualityValue).reversed()
						.thenComparing(MediaType::isWildcardType)
						.thenComparing(MediaType::isWildcardSubtype)
						.thenComparing((MediaType mediaType) -> mediaType.getParameters().size(), Comparator.reverseOrder())
		);
		return mediaTypes;
	}

	private AnagramRenderer defaultRenderer() {
		return renderers.stream()
				.filter(renderer -> renderer.supports(MediaType.APPLICATION_JSON))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Default JSON anagram renderer is not configured"));
	}
}
