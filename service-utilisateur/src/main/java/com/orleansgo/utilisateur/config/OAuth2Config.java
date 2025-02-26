package com.orleansgo.utilisateur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class OAuth2Config {

    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
                             OAuth2AuthorizedClientRepository authorizedClientRepository) {
        
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = 
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository, 
                authorizedClientRepository);
        
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        
        return WebClient.builder()
            .filter(logRequest())
            .filter(logResponse())
            .apply(oauth2.oauth2Configuration())
            .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // Log request details
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            // Log response details
            return Mono.just(clientResponse);
        });
    }
}
