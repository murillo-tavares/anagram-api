package murillo.tavares.anagram_api.adapter.in.web;

import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.adapter.in.web.response.AnagramResponse;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.domain.model.Anagram;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anagrams")
@RequiredArgsConstructor
public class AnagramController {

	private final GetDailyAnagramUseCase getDailyAnagramUseCase;

	@GetMapping("/daily")
	public AnagramResponse getDailyAnagram() {
		Anagram dailyAnagram = getDailyAnagramUseCase.getDailyAnagram();
		return AnagramResponse.from(dailyAnagram);
	}
}
