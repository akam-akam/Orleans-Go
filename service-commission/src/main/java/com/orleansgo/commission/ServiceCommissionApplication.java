
package com.orleansgo.commission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceCommissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceCommissionApplication.class, args);
    }
}
