package murillo.tavares.anagram_api.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;

public record SubmitAnagramAnswerRequest(
		@NotBlank(message = "answer must not be blank")
		String answer
) {
}
