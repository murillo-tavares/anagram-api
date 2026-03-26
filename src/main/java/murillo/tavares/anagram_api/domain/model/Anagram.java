package murillo.tavares.anagram_api.domain.model;

import java.util.List;

public record Anagram(
		String task,
		List<String> solutions
) {

	public Anagram {
		solutions = List.copyOf(solutions);
	}
}
