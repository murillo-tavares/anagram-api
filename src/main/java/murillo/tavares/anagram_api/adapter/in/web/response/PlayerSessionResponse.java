package murillo.tavares.anagram_api.adapter.in.web.response;

public record PlayerSessionResponse(
		boolean identified,
		String playerTag
) {
}
