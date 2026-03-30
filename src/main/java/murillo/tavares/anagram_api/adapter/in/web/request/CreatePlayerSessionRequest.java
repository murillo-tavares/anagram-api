package murillo.tavares.anagram_api.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import murillo.tavares.anagram_api.domain.model.PlayerIdentity;

public record CreatePlayerSessionRequest(
		@NotBlank(message = "name must not be blank")
		@Size(
				min = PlayerIdentity.MIN_NAME_LENGTH,
				max = PlayerIdentity.MAX_NAME_LENGTH,
				message = "name must have between 3 and 20 characters"
		)
		@Pattern(
				regexp = PlayerIdentity.NAME_PATTERN,
				message = "name must contain only letters, numbers or underscore"
		)
		String name
) {
}
