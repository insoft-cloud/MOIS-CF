package org.openpaas.paasta.portal.common.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


//@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
public class CommonApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApiApplication.class, args);
    }

}

