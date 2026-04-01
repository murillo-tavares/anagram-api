package murillo.tavares.anagram_api.application.service;

import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.application.port.in.SubmitDailyAnagramAnswerUseCase;
import murillo.tavares.anagram_api.application.port.out.GenerateAnagramPort;
import murillo.tavares.anagram_api.application.port.out.ManageDailyAnagramPort;
import murillo.tavares.anagram_api.common.date.DateUtils;
import murillo.tavares.anagram_api.domain.model.Anagram;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyAnagramService implements GetDailyAnagramUseCase, SubmitDailyAnagramAnswerUseCase {

	private final GenerateAnagramPort generateAnagramPort;
	private final ManageDailyAnagramPort manageDailyAnagramPort;

	@Override
	@Transactional
	public DailyAnagram getDailyAnagram() {
		LocalDate today = DateUtils.currentDate();
		return manageDailyAnagramPort.findByDate(today)
				.orElseGet(() -> createDailyAnagram(today));
	}

	@Override
	@Transactional
	public DailyAnagram submitAnswer(String answer, String foundBy) {
		DailyAnagram dailyAnagram = getDailyAnagram();

		if (dailyAnagram.isNewValidSolution(answer)) {
			return manageDailyAnagramPort.markSolutionAsFound(dailyAnagram, answer, foundBy);
		}

		return dailyAnagram;
	}

	private DailyAnagram createDailyAnagram(LocalDate date) {
		Anagram generatedAnagram = generateAnagramPort.generateAnagram();
		DailyAnagram dailyAnagram = new DailyAnagram(date, generatedAnagram);

		return manageDailyAnagramPort.save(dailyAnagram);
	}
}
