package murillo.tavares.anagram_api.application.port.in;

import murillo.tavares.anagram_api.domain.model.DailyAnagram;

public interface SubmitDailyAnagramAnswerUseCase {

	DailyAnagram submitAnswer(String answer);
}
