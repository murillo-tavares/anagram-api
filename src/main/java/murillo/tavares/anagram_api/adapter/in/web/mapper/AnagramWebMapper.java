package murillo.tavares.anagram_api.adapter.in.web.mapper;

import murillo.tavares.anagram_api.adapter.in.web.response.AnagramResponse;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagramSolution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnagramWebMapper {

	@Mapping(target = "task", source = "task")
	@Mapping(target = "solutions", expression = "java(toSolutions(dailyAnagram))")
	AnagramResponse toResponse(DailyAnagram dailyAnagram);

	default List<String> toSolutions(DailyAnagram dailyAnagram) {
		return dailyAnagram.solutions().stream()
				.map(DailyAnagramSolution::formattedValue)
				.toList();
	}
}
