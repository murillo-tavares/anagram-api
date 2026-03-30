package murillo.tavares.anagram_api.adapter.in.web.session;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlayerSessionConfiguration {

	@Bean
	ServletListenerRegistrationBean<PlayerSessionListener> playerSessionListenerRegistrationBean(
			PlayerTagRegistry playerTagRegistry
	) {
		return new ServletListenerRegistrationBean<>(new PlayerSessionListener(playerTagRegistry));
	}
}
