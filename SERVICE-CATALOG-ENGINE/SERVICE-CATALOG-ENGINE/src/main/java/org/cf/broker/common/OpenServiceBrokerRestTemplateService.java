package org.cf.broker.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class OpenServiceBrokerRestTemplateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenServiceBrokerRestTemplateService.class);
    private final RestTemplate restTemplate;


    public OpenServiceBrokerRestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public <T> ResponseEntity send(String reqUrl, String id, String pwd, String version, HttpMethod httpMethod, Object bodyObject, Class<T> responseType) throws HttpStatusCodeException, Exception {
        HttpHeaders reqHeaders = new HttpHeaders();
        String base64Authorization = "Basic " + Base64.getEncoder().encodeToString((id + ":" + pwd).getBytes(StandardCharsets.UTF_8));
        reqHeaders.add(Constants.AUTHORIZATION, base64Authorization);
        reqHeaders.add(Constants.X_BROKER_API_VERSION, version);
        HttpEntity<Object> reqEntity = new HttpEntity<>(bodyObject, reqHeaders);
        LOGGER.info("<T> T SEND :: REQUEST: {} BASE-URL: {}, CONTENT-TYPE: {}", httpMethod, reqUrl, reqHeaders.get(Constants.X_BROKER_API_VERSION));
        LOGGER.info("<T> T SEND :: RESPONSE-BODY: {}", bodyObject);
        ResponseEntity<T> resEntity = restTemplate.exchange(reqUrl, httpMethod, reqEntity, responseType);
        if (resEntity.getBody() != null) {
            LOGGER.info("RESPONSE BODY : {}", resEntity.getBody());
            LOGGER.info("RESPONSE CODE : {}", resEntity.getStatusCode());
        } else {
            //LOGGER.error("RESPONSE-TYPE: RESPONSE BODY IS NULL");
        }
        return resEntity;
    }
}
