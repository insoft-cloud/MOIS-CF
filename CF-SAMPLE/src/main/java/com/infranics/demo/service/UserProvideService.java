package com.infranics.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserProvideService {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Object getServices(){
        return System.getenv("VCAP_SERVICES");
    }


    public Object getUser_Provide() throws JsonProcessingException {
        String vcap_services = System.getenv("VCAP_SERVICES");
        List user_provide = new ArrayList();
        if (vcap_services != null && vcap_services.length() > 0) {
            Map<String, List<Map<String, Object>>> user_services =
                    this.objectMapper.readValue(vcap_services,new TypeReference<Map<String, List<Map<String, Object>>>>() {});
            user_services.get("user-provided").forEach(services -> {
                        user_provide.add(services.get("name"));
            });
//            user_services.entrySet().forEach( entry -> {
//                entry.getValue().forEach(data -> {
//                    user_provide.add(data);
//                });
//            });
        }

        return user_provide;
    }


    public Map user_provide_name(String user_provide_name) throws JsonProcessingException {
        String vcap_services = System.getenv("VCAP_SERVICES");
        AtomicReference<Map<String, Object>> user_provide = new AtomicReference<>();
        if (vcap_services != null && vcap_services.length() > 0) {
            Map<String, List<Map<String, Object>>> user_services =
                    this.objectMapper.readValue(vcap_services, new TypeReference<Map<String, List<Map<String, Object>>>>() {
                    });
            user_services.get("user-provided").forEach(services -> {
                if(services.get("instance_name").equals(user_provide_name)) {
                    user_provide.set(services);
                }});
        };
        return user_provide.get();
    }
}
