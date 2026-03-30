package murillo.tavares.anagram_api.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public abstract class ApiException extends ErrorResponseException {

	protected ApiException(HttpStatus status, String title, String detail) {
		this(status, title, detail, null);
	}

	protected ApiException(HttpStatus status, String title, String detail, Throwable cause) {
		super(status, createProblemDetail(status, title, detail), cause);
	}

	private static ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
		problemDetail.setTitle(title);
		return problemDetail;
	}
}
