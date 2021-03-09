package org.cf.servicebroker.config;

import org.openpaas.paasta.bosh.director.BoshDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = "org.cf.servicebroker.controller")
public class BoshConfig {

    @Value("${bosh.client_id}")
    public String client_id;
    @Value("${bosh.client_secret}")
    public String client_secret;
    @Value("${bosh.url}")
    public String url;
    @Value("${bosh.oauth_url}")
    public String oauth_url;

    private static final Logger LOGGER = LoggerFactory.getLogger(BoshConfig.class);



    @Bean
    BoshDirector boshDirector(){
        LOGGER.info(client_id);
        LOGGER.info(client_secret);
        LOGGER.info(url);
        LOGGER.info(oauth_url);
        BoshDirector boshDirector = new BoshDirector(client_id,client_secret,url,oauth_url);
        return boshDirector;
    }
}
