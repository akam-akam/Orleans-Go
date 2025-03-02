
package com.orleansgo.evenement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceEvenementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceEvenementApplication.class, args);
    }
}
