package org.openpaas.paasta.portal.api.config;

import org.openpaas.paasta.bosh.director.BoshDirector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "org.openpaas.paasta.portal.api.rabbitmq.service")
public class BoshConfig {

    @Value("${bosh.client_id}")
    public String client_id;
    @Value("${bosh.client_secret}")
    public String client_secret;
    @Value("${bosh.url}")
    public String url;
    @Value("${bosh.oauth_url}")
    public String oauth_url;


    @Bean
    BoshDirector boshDirector(){
        BoshDirector boshDirector = new BoshDirector(client_id,client_secret,url,oauth_url);
        return boshDirector;
    }
}
