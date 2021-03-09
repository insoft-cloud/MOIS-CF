//package org.openpaas.paasta.portal.web.admin.config.security.userdetail;
//
//import org.openpaas.paasta.portal.web.admin.common.User;
//import org.openpaas.paasta.portal.web.admin.common.UserList;
////import org.openpaas.paasta.portal.web.admin.entity.ConfigEntity;
////import org.openpaas.paasta.portal.web.admin.respository.ConfigRepository;
//import org.openpaas.paasta.portal.web.admin.service.CommonService;
////import org.openpaas.paasta.portal.web.admin.service.ConfigService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by mg on 2016-05-11.
// */
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private CommonService commonService;
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        List role = new ArrayList();
//
//        role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        User user = new User("admin", "admin", role);
//        return user;
//    }
//
//    public User loginByUsernameAndPassword(String password) throws UsernameNotFoundException {
//
//        Map map = commonService.procGetUserRestTemplate(password);
//
//
//        List role = new ArrayList();
//        role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//
//        User user = new User(map.get("user_name").toString(), password, role);
//        user.setToken(password);
//        user.setExpireDate(1000L);
//        user.setName(map.get("user_name").toString());
//        user.setToken_id("");
//        System.out.println(user.toString());
//        return user;
//    }
//}
