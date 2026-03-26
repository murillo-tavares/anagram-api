package murillo.tavares.anagram_api.adapter.out.http.config;

import murillo.tavares.anagram_api.adapter.out.http.client.FreeApiAnagramClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = FreeApiAnagramClient.class)
public class FeignClientsConfig {
}
