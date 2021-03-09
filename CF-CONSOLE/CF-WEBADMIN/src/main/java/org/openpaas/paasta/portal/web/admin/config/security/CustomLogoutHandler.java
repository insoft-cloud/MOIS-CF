//package org.openpaas.paasta.portal.web.admin.config.security;
//
//import org.openpaas.paasta.portal.web.admin.common.Constants;
//import org.openpaas.paasta.portal.web.admin.common.User;
//import org.openpaas.paasta.portal.web.admin.service.CommonService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Service
//public class CustomLogoutHandler implements LogoutHandler {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLogoutHandler.class);
//
//    final CommonService commonService;
//
//    public CustomLogoutHandler(CommonService commonService) {
//        this.commonService = commonService;
//    }
//
//
//    @Override
//    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//        try {
//            HttpHeaders reqHeaders = new HttpHeaders();
//            LOGGER.info(authentication.getPrincipal().toString());
//            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            reqHeaders.add("Authorization", user.getToken_type() + " " + user.getToken());
//            commonService.procApiRestTemplate("/oauth/token/revoke/" + user.getToken() , HttpMethod.DELETE, null, Constants.UAA_API);
//        }catch (Exception e){
//            LOGGER.error(e.getMessage());
//        }
//
//    }
//}
