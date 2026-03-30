package murillo.tavares.anagram_api.adapter.out.http;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.adapter.out.http.client.FreeApiAnagramClient;
import murillo.tavares.anagram_api.adapter.out.http.dto.FreeApiAnagramResponse;
import murillo.tavares.anagram_api.adapter.out.http.exception.AnagramProviderException;
import murillo.tavares.anagram_api.adapter.out.http.mapper.FreeApiAnagramMapper;
import murillo.tavares.anagram_api.application.port.out.GenerateAnagramPort;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreeApiAnagramFeignAdapter implements GenerateAnagramPort {

	private final FreeApiAnagramClient freeApiAnagramClient;
	private final FreeApiAnagramMapper freeApiAnagramMapper;

	@Override
	public Anagram generateAnagram() {
		try {
			FreeApiAnagramResponse response = freeApiAnagramClient.fetchAnagram();

			if (response == null) {
				throw new AnagramProviderException("FreeAPI anagram provider returned an empty response");
			}

			return freeApiAnagramMapper.toDomain(response);
		} catch (FeignException exception) {
			throw new AnagramProviderException("Failed to fetch anagram from FreeAPI provider", exception);
		}
	}
}
