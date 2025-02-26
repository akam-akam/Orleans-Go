package com.orleansgo.utilisateur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Service Utilisateur - OrleansGO")
                        .version("1.0")
                        .description("API pour la gestion des utilisateurs"));
    }
}
