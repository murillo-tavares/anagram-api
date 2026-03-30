package murillo.tavares.anagram_api.adapter.out.persistence.mapper;

import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramEntity;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagramSolution;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyAnagramPersistenceMapper {

	@Mapping(target = "date", source = "puzzleDate")
	@Mapping(target = "task", source = "task")
	@Mapping(target = "solutions", expression = "java(toSolutions(entity))")
	DailyAnagram toDomain(DailyAnagramEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "puzzleDate", ignore = true)
	@Mapping(target = "task", ignore = true)
	@Mapping(target = "solutions", ignore = true)
	DailyAnagramEntity toEntity(DailyAnagram dailyAnagram);

	@ObjectFactory
	default DailyAnagramEntity createEntity(DailyAnagram dailyAnagram) {
		return new DailyAnagramEntity(dailyAnagram.date(), dailyAnagram.task());
	}

	@AfterMapping
	default void addSolutions(DailyAnagram dailyAnagram, @MappingTarget DailyAnagramEntity entity) {
		for (DailyAnagramSolution solution : dailyAnagram.solutions()) {
			entity.addSolution(solution.answer(), solution.found());
		}
	}

	default List<DailyAnagramSolution> toSolutions(DailyAnagramEntity entity) {
		return entity.getSolutions().stream()
				.map(solution -> new DailyAnagramSolution(solution.getAnswer(), solution.isFound()))
				.toList();
	}
}
