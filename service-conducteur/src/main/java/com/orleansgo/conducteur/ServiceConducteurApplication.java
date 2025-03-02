
package com.orleansgo.conducteur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceConducteurApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceConducteurApplication.class, args);
    }
}
package com.orleansgo.conducteur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceConducteurApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ServiceConducteurApplication.class, args);
    }
}
