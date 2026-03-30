package murillo.tavares.anagram_api.adapter.in.web.session;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerSessionListener implements HttpSessionListener {

	private final PlayerTagRegistry playerTagRegistry;

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		playerTagRegistry.releaseBySessionId(event.getSession().getId());
	}
}
