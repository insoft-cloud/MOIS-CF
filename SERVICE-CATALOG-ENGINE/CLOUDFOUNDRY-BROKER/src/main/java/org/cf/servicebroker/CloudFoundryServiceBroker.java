package org.cf.servicebroker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudFoundryServiceBroker {
    public static void main(String[] args) {
        SpringApplication.run(CloudFoundryServiceBroker.class, args);
    }
}
