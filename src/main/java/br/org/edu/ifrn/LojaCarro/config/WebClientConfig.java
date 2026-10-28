package br.org.edu.ifrn.LojaCarro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${usuario.api.url:http://localhost:8081}")
    private String usuarioApiUrl;

    @Bean
    public WebClient usuarioWebClient() {
        return WebClient.builder()
                .baseUrl(usuarioApiUrl)
                .build();
    }
}