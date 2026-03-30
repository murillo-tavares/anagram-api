package murillo.tavares.anagram_api.adapter.in.web.mapper;

import murillo.tavares.anagram_api.adapter.in.web.response.PlayerSessionResponse;
import murillo.tavares.anagram_api.domain.model.PlayerIdentity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerSessionWebMapper {

	default PlayerSessionResponse toResponse(PlayerIdentity playerIdentity) {
		return new PlayerSessionResponse(true, playerIdentity.playerTag());
	}

	default PlayerSessionResponse anonymousResponse() {
		return new PlayerSessionResponse(false, null);
	}
}
