package murillo.tavares.anagram_api.adapter.in.web.error;

import murillo.tavares.anagram_api.adapter.out.http.exception.AnagramProviderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(AnagramProviderException.class)
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	public ProblemDetail handleAnagramProviderException(AnagramProviderException exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, exception.getMessage());
		problemDetail.setTitle("External anagram provider failure");
		return problemDetail;
	}
}
