package murillo.tavares.anagram_api.adapter.out.http.client;

import murillo.tavares.anagram_api.adapter.out.http.dto.FreeApiAnagramResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
		name = "freeApiAnagramClient",
		url = "${freeapi-anagram-provider.base-url}"
)
public interface FreeApiAnagramClient {

	@GetMapping("/")
	FreeApiAnagramResponse fetchAnagram();
}
