package murillo.tavares.anagram_api.application.port.out;

import murillo.tavares.anagram_api.domain.model.DailyAnagram;

import java.time.LocalDate;
import java.util.Optional;

public interface ManageDailyAnagramPort {

	Optional<DailyAnagram> findByDate(LocalDate date);

	DailyAnagram save(DailyAnagram dailyAnagram);

	DailyAnagram markSolutionAsFound(DailyAnagram dailyAnagram, String answer);
}
