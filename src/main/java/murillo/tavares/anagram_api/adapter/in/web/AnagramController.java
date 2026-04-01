package murillo.tavares.anagram_api.adapter.in.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import murillo.tavares.anagram_api.adapter.in.web.mapper.AnagramWebMapper;
import murillo.tavares.anagram_api.adapter.in.web.request.SubmitAnagramAnswerRequest;
import murillo.tavares.anagram_api.adapter.in.web.response.AnagramResponse;
import murillo.tavares.anagram_api.adapter.in.web.session.PlayerSessionManager;
import murillo.tavares.anagram_api.application.port.in.GetDailyAnagramUseCase;
import murillo.tavares.anagram_api.application.port.in.SubmitDailyAnagramAnswerUseCase;
import murillo.tavares.anagram_api.domain.model.DailyAnagram;
import murillo.tavares.anagram_api.domain.model.PlayerIdentity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/anagrams")
@RequiredArgsConstructor
public class AnagramController {

	private final GetDailyAnagramUseCase getDailyAnagramUseCase;
	private final SubmitDailyAnagramAnswerUseCase submitDailyAnagramAnswerUseCase;
	private final AnagramWebMapper anagramWebMapper;
	private final PlayerSessionManager playerSessionManager;

	@GetMapping("/daily")
	public AnagramResponse getDailyAnagram() {
		DailyAnagram dailyAnagram = getDailyAnagramUseCase.getDailyAnagram();
		return anagramWebMapper.toResponse(dailyAnagram);
	}

	@PostMapping("/daily/answers")
	public AnagramResponse submitAnswer(@Valid @RequestBody SubmitAnagramAnswerRequest request, HttpSession session) {
		String foundBy = playerSessionManager.getCurrentPlayer(session)
				.map(PlayerIdentity::playerTag)
				.orElse(null);
		DailyAnagram dailyAnagram = submitDailyAnagramAnswerUseCase.submitAnswer(request.answer(), foundBy);
		return anagramWebMapper.toResponse(dailyAnagram);
	}
}
