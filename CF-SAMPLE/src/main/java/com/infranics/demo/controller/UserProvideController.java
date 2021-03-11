package com.infranics.demo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.infranics.demo.service.UserProvideService;
import org.json.JSONArray;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class UserProvideController {
    final UserProvideService service;

    public UserProvideController(UserProvideService service) {
        this.service = service;
    }


    @GetMapping("/user-provide")
    public Object getUser_Provide() throws JsonProcessingException {
        ModelAndView mv = new ModelAndView("user-provide");
        List<JSONArray> list = new ArrayList<JSONArray>();
        mv.addObject("list", service.getUser_Provide());
        return mv;
    }

    @GetMapping("/user-provide/{service_name}")
    public Map user_provide_name(@PathVariable String service_name) throws JsonProcessingException {
        return service.user_provide_name(service_name);
    }


}
