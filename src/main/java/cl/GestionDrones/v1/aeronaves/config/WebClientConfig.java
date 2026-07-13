package cl.GestionDrones.v1.aeronaves.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.url.empresasproveedoras}")
    private String empresasProveedorasUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient empresasProveedorasWebClient(WebClient.Builder builder) {
        return builder.baseUrl(empresasProveedorasUrl).build();
    }
}
