package murillo.tavares.anagram_api.adapter.in.web.mapper;

import murillo.tavares.anagram_api.adapter.in.web.response.AnagramResponse;
import murillo.tavares.anagram_api.adapter.in.web.response.AnagramSolutionResponse;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagramSolution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnagramWebMapper {

	@Mapping(target = "letters", source = "letters")
	@Mapping(target = "solutions", expression = "java(toSolutions(dailyAnagram))")
	AnagramResponse toResponse(DailyAnagram dailyAnagram);

	default List<AnagramSolutionResponse> toSolutions(DailyAnagram dailyAnagram) {
		return dailyAnagram.solutions().stream()
				.map(solution -> new AnagramSolutionResponse(solution.formattedValue(), solution.foundBy()))
				.toList();
	}
}
