//package org.cf.broker.config;
//
//import org.cf.broker.model.BrokerApiVersion;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
//import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ComponentScan(
//        basePackages = {"org.cf.broker"}
//)
//@ConditionalOnWebApplication
//@AutoConfigureAfter({WebMvcAutoConfiguration.class})
//public class ServiceBrokerAutoConfiguration {
//    public ServiceBrokerAutoConfiguration() {
//    }
//
//    @Bean
//    @ConditionalOnMissingBean({BrokerApiVersion.class})
//    public BrokerApiVersion brokerApiVersion() {
//        return new BrokerApiVersion("2.x, 3.x");
//    }
//}
