package org.openpaas.paasta.portal.web.admin.controller;

import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.Constants;
import org.openpaas.paasta.portal.web.admin.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CallBackController extends Common {

    @GetMapping("/callback")
    public ModelAndView getUserInfoMain() {
        return callBackService.getCallBackMain();
    }


    @PostMapping("/callback")
    public @ResponseBody  Map<String, Object> autoLogin(@RequestParam Map<String, Object> data){
       return callBackService.autoLogin(data);
    }

//    @PostMapping("/getToken")
//    private boolean getTokens(@RequestBody Map<String, Object> data) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        boolean result = (boolean)commonService.procApiRestTemplate("/check_token?token=" + user.getToken(), HttpMethod.GET, null, Constants.CF_API).get("result");
//        if(result){
//            long second = System.currentTimeMillis()/1000;
//            if(user.getExpireDate() < second - 10){
//                return false;
//            }
//        }
//        return true;
//    }
}
