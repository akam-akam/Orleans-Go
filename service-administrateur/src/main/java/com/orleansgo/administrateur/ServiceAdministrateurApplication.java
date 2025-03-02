
package com.orleansgo.administrateur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@OpenAPIDefinition(
    info = @Info(
        title = "API Service Administrateur OrleansGO",
        version = "1.0",
        description = "API pour la gestion administrative de la plateforme OrleansGO"
    )
)
public class ServiceAdministrateurApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAdministrateurApplication.class, args);
    }
}
