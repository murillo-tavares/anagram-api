package murillo.tavares.anagram_api.adapter.out.http.mapper;

import murillo.tavares.anagram_api.adapter.out.http.dto.FreeApiAnagramResponse;
import murillo.tavares.anagram_api.common.text.TextUtils;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface FreeApiAnagramMapper {

	@Mapping(target = "letters", expression = "java(toLetters(response))")
	@Mapping(target = "solutions", expression = "java(toSolutions(response))")
	Anagram toDomain(FreeApiAnagramResponse response);

	default String toLetters(FreeApiAnagramResponse response) {
		return TextUtils.shuffleLetters(response.task());
	}

	default List<String> toSolutions(FreeApiAnagramResponse response) {
		return Stream.concat(response.solutions().stream(), Stream.of(response.task()))
				.toList();
	}
}
