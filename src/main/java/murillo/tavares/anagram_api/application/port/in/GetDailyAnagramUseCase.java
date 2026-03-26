package murillo.tavares.anagram_api.application.port.in;

import murillo.tavares.anagram_api.domain.model.Anagram;

public interface GetDailyAnagramUseCase {

	Anagram getDailyAnagram();
}
