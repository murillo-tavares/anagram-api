package murillo.tavares.anagram_api.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramEntity;
import murillo.tavares.anagram_api.adapter.out.persistence.entity.DailyAnagramSolutionEntity;
import murillo.tavares.anagram_api.adapter.out.persistence.mapper.DailyAnagramPersistenceMapper;
import murillo.tavares.anagram_api.adapter.out.persistence.repository.DailyAnagramJpaRepository;
import murillo.tavares.anagram_api.adapter.out.persistence.repository.DailyAnagramSolutionJpaRepository;
import murillo.tavares.anagram_api.common.text.TextUtils;
import murillo.tavares.anagram_api.application.port.out.ManageDailyAnagramPort;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DailyAnagramPersistenceAdapter implements ManageDailyAnagramPort {

	private final DailyAnagramJpaRepository dailyAnagramJpaRepository;
	private final DailyAnagramSolutionJpaRepository dailyAnagramSolutionJpaRepository;
	private final DailyAnagramPersistenceMapper dailyAnagramPersistenceMapper;

	@Override
	public Optional<DailyAnagram> findByDate(LocalDate date) {
		return dailyAnagramJpaRepository.findByPuzzleDate(date)
				.map(dailyAnagramPersistenceMapper::toDomain);
	}

	@Override
	public DailyAnagram save(DailyAnagram dailyAnagram) {
		DailyAnagramEntity entity = dailyAnagramPersistenceMapper.toEntity(dailyAnagram);
		DailyAnagramEntity savedEntity = dailyAnagramJpaRepository.save(entity);
		return dailyAnagramPersistenceMapper.toDomain(savedEntity);
	}

	@Override
	public DailyAnagram markSolutionAsFound(DailyAnagram dailyAnagram, String answer) {
		DailyAnagramSolutionEntity entity = dailyAnagramSolutionJpaRepository
				.findByDailyAnagramPuzzleDateAndAnswer(dailyAnagram.date(), TextUtils.normalize(answer))
				.orElseThrow(() -> new IllegalStateException("Stored daily anagram solution was not found"));

		entity.markAsFound();
		return dailyAnagramPersistenceMapper.toDomain(entity.getDailyAnagram());
	}
}
