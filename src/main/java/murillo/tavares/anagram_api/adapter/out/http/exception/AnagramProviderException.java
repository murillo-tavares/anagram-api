package murillo.tavares.anagram_api.adapter.out.http.exception;

import murillo.tavares.anagram_api.common.error.ApiException;
import org.springframework.http.HttpStatus;

public class AnagramProviderException extends ApiException {

	private static final HttpStatus STATUS = HttpStatus.BAD_GATEWAY;
	private static final String TITLE = "External anagram provider failure";

	public AnagramProviderException(String message) {
		super(STATUS, TITLE, message);
	}

	public AnagramProviderException(String message, Throwable cause) {
		super(STATUS, TITLE, message, cause);
	}
}
