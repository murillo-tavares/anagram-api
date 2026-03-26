package murillo.tavares.anagram_api.adapter.in.web.response;

import murillo.tavares.anagram_api.domain.model.Anagram;

import java.util.List;

public record AnagramResponse(
		String task,
		List<String> solutions
) {

	public static AnagramResponse from(Anagram anagram) {
		return new AnagramResponse(
				anagram.task(),
				anagram.solutions()
		);
	}
}
