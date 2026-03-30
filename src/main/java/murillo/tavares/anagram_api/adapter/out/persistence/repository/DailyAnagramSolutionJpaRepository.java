package murillo.tavares.anagram_api.adapter.out.persistence.repository;

import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramSolutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyAnagramSolutionJpaRepository extends JpaRepository<DailyAnagramSolutionEntity, Long> {

	Optional<DailyAnagramSolutionEntity> findByDailyAnagramPuzzleDateAndAnswer(LocalDate puzzleDate, String answer);
}
