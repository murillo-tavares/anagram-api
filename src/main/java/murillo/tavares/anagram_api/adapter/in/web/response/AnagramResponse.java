package murillo.tavares.anagram_api.adapter.in.web.response;

import java.util.List;

public record AnagramResponse(
		String task,
		int totalSolutions,
		List<String> foundSolutions
) {
}
