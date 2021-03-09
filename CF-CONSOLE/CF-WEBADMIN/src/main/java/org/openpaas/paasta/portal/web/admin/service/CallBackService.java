package org.openpaas.paasta.portal.web.admin.service;

import org.openpaas.paasta.portal.web.admin.common.User;
import org.openpaas.paasta.portal.web.admin.controller.CallBackController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Service
public class CallBackService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallBackController.class);

    @Autowired
    CommonService commonService;

    public ModelAndView getCallBackMain() {
        return new ModelAndView() {{
            setViewName("callback");
        }};
    }

    public Map<String, Object> autoLogin(Map<String, Object> data){
        return data;
    }
}
