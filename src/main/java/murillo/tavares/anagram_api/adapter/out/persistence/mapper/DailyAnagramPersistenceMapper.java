package murillo.tavares.anagram_api.adapter.out.persistence.mapper;

import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramEntity;
import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramSolutionEntity;
import murillo.tavares.anagram_api.domain.model.Anagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DailyAnagramPersistenceMapper {

	@Mapping(target = "date", source = "puzzleDate")
	@Mapping(target = "anagram", expression = "java(toAnagram(entity))")
	@Mapping(target = "foundSolutions", expression = "java(toFoundSolutions(entity))")
	DailyAnagram toDomain(DailyAnagramEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "puzzleDate", ignore = true)
	@Mapping(target = "task", ignore = true)
	@Mapping(target = "solutions", ignore = true)
	DailyAnagramEntity toEntity(DailyAnagram dailyAnagram);

	@ObjectFactory
	default DailyAnagramEntity createEntity(DailyAnagram dailyAnagram) {
		return new DailyAnagramEntity(dailyAnagram.date(), dailyAnagram.anagram().task());
	}

	@AfterMapping
	default void addSolutions(DailyAnagram dailyAnagram, @MappingTarget DailyAnagramEntity entity) {
		for (String solution : dailyAnagram.anagram().solutions()) {
			entity.addSolution(solution, dailyAnagram.foundSolutions().contains(solution));
		}
	}

	default Anagram toAnagram(DailyAnagramEntity entity) {
		return new Anagram(
				entity.getTask(),
				entity.getSolutions().stream()
						.map(DailyAnagramSolutionEntity::getAnswer)
						.toList()
		);
	}

	default List<String> toFoundSolutions(DailyAnagramEntity entity) {
		return entity.getSolutions().stream()
				.filter(DailyAnagramSolutionEntity::isFound)
				.map(DailyAnagramSolutionEntity::getAnswer)
				.toList();
	}
}
