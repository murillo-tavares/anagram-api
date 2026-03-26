package murillo.tavares.anagram_api.adapter.out.http.exception;

public class AnagramProviderException extends RuntimeException {

	public AnagramProviderException(String message) {
		super(message);
	}

	public AnagramProviderException(String message, Throwable cause) {
		super(message, cause);
	}
}
