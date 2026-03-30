package murillo.tavares.anagram_api.adapter.out.persistence.repository;

import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyAnagramJpaRepository extends JpaRepository<DailyAnagramEntity, Long> {

	@EntityGraph(attributePaths = "solutions")
	Optional<DailyAnagramEntity> findByPuzzleDate(LocalDate puzzleDate);
}
