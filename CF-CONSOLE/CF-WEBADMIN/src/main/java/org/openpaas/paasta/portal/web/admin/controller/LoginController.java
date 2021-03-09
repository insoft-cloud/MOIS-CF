package org.openpaas.paasta.portal.web.admin.controller;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.User;
import org.openpaas.paasta.portal.web.admin.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Login Controller
 *
 * @author nawkm
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
public class LoginController extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    @Autowired
    public CommonService commonService;
    /**
     * 로그인 화면
//     *
//     * @param error   the error
//     * @param logout  the logout
//     * @param locale  the locale
     * @param request the request
     * @return ModelAndView model
     */
    @GetMapping(value = {"/", "/index"})
    public ModelAndView loginPage(HttpServletRequest request) {

        KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
        if(principal == null){
            ModelAndView mv = new ModelAndView();
            mv.setViewName("/index");
            return mv;
        }
        commonService.getTokens();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main/main");
        return mv;
    }


//    @GetMapping(value = {"/sso/login"})
//    public Map<String, Object>  getAutoLogin() {
//        LOGGER.info("여기옴");
//        return null;
//    }
//
//    @PostMapping(value = {"/login/autoLogin"})
//    public  Map<String, Object> postAutoLogin(@RequestBody Map<String, String> param) {
//        String endpoint = "/oauth/token?response_type=code&client_id=" +
//                commonService.getPortalClient() +"&client_secret=" + commonService.getClientSecret() + "&redirect_uri=" + param.get("redirectUrl") + ("?returnUrl=" + param.get("returnUrl")) + "&grant_type=authorization_code&code=" + param.get("code");
//        LOGGER.info(endpoint);
//        LOGGER.info(param.toString());
//        HttpHeaders reqHeaders = new HttpHeaders();
//        reqHeaders.add("Content-Type", "application/x-www-form-urlencoded");
//        reqHeaders.add("Accept", "application/json");
//        Map access = commonService.procLogOutRestTemplate(endpoint, reqHeaders , HttpMethod.POST, null);
//        if(access.get("data") != null){
//            List role = new ArrayList();
//            if(param.get("user_se_cd").equals("A01")){
//                role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//            }else {
//                role.add(new SimpleGrantedAuthority("ROLE_USER"));
//            }
//            Map data = (Map)access.get("data");
//            String type = data.get("token_type").toString();
//
//            byte[] bytes;
//            try {
//                bytes = (commonService.getPortalClient() + ":" + commonService.getClientSecret()).getBytes("UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                access.put("result", false);
//                access.put("msg", e.getMessage());
//                return access;
//            }
//            reqHeaders.add(AUTHORIZATION_HEADER_KEY, "Basic " + Base64.getEncoder().encodeToString(bytes));
//            Map check = (Map)commonService.procLogOutRestTemplate("/check_token?token=" + data.get("access_token").toString(), reqHeaders , HttpMethod.GET, null).get("data");
//            User user = new User(check.get("user_name").toString(), "admin", role);
//            user.setToken_type(type.substring(0, 1).toUpperCase() + type.substring(1));
//            user.setToken(data.get("access_token").toString());
//            user.setToken_id(data.get("id_token").toString());
//            user.setName(check.get("user_name").toString());
//            user.setExpireDate(Long.valueOf(check.get("exp").toString()));
//
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());;
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//        }
//        return access;
//    }
//
//    @DeleteMapping(value = {"/login/logout"})
//    public Map<String, Object>  logoutEndPoint(){
//        String endpoint = commonService.getDomain() + "/logout.do?client_id=" +
//                commonService.getPortalClient() +"&redirect=" + commonService.getLogoutRedirectUrl();
//        Map map = new HashMap<String, Object>();
//        map.put("result", endpoint);
//        return map;
//    }

}
