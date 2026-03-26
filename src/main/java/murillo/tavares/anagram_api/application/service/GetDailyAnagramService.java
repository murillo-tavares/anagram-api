package murillo.tavares.anagram_api.application.service;

import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.application.port.out.GenerateAnagramPort;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDailyAnagramService implements GetDailyAnagramUseCase {

	private final GenerateAnagramPort generateAnagramPort;

	@Override
	public Anagram getDailyAnagram() {
		return generateAnagramPort.generateAnagram();
	}
}
