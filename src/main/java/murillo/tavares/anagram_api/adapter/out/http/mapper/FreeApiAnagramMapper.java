package murillo.tavares.anagram_api.adapter.out.http.mapper;

import murillo.tavares.anagram_api.adapter.out.http.dto.FreeApiAnagramResponse;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FreeApiAnagramMapper {

	Anagram toDomain(FreeApiAnagramResponse response);
}
