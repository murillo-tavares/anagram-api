package murillo.tavares.anagram_api.adapter.in.web.mapper;

import murillo.tavares.anagram_api.adapter.in.web.response.AnagramResponse;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnagramWebMapper {

	@Mapping(target = "task", source = "anagram.task")
	@Mapping(target = "totalSolutions", expression = "java(dailyAnagram.anagram().totalSolutionsCount())")
	AnagramResponse toResponse(DailyAnagram dailyAnagram);
}
