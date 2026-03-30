package murillo.tavares.anagram_api.adapter.in.web.error;

import murillo.tavares.anagram_api.common.error.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ProblemDetail> handleApiException(ApiException exception) {
		return ResponseEntity.status(exception.getStatusCode()).body(exception.getBody());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal server error");
		problemDetail.setTitle("Internal server error");
		return ResponseEntity.internalServerError().body(problemDetail);
	}
}
