package murillo.tavares.anagram_api.adapter.in.web.response;

import java.util.List;

public record AnagramResponse(
		String letters,
		List<String> solutions
) {
}
