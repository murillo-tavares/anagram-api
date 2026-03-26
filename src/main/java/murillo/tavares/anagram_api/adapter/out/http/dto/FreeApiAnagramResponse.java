package murillo.tavares.anagram_api.adapter.out.http.dto;

import java.util.List;

public record FreeApiAnagramResponse(
		String task,
		List<String> solutions
) {
}
