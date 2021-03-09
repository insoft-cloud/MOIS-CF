//package org.openpaas.paasta.portal.web.admin.config.security;
//
///**
// * Created by mg on 2016-05-12.
// */
//
//import org.openpaas.paasta.portal.web.admin.common.User;
//import org.openpaas.paasta.portal.web.admin.config.security.userdetail.CustomUserDetailsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.*;
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
//
//    @Autowired
//    CustomUserDetailsService customUserDetailsService;
//
//    /*
//        @Autowired
//        private BCryptPasswordEncoder bCryptPasswordEncoder;
//    */
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//
//        Collection<? extends GrantedAuthority> authorities = null;
//
//        User users = null;
//
//        try {
//            users = customUserDetailsService.loginByUsernameAndPassword(password);
//            LOGGER.info("USER :::" + users.toString());
//        } catch (UsernameNotFoundException e) {
//            LOGGER.error(e.toString());
//            throw new UsernameNotFoundException(e.getMessage());
//        } catch (BadCredentialsException e) {
//            LOGGER.error(e.toString());
//            throw new BadCredentialsException(e.getMessage());
//        } catch (Exception e) {
//            LOGGER.error(e.toString());
//            throw new BadCredentialsException("");
//        }
//
//
//        List role = new ArrayList();
//        role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(users, password, role);
//        usernamePasswordAuthenticationToken.setDetails(users);
//        return usernamePasswordAuthenticationToken;
//    }
//
//
//
//    @Override
//    public boolean supports(Class<?> arg0) {
//        return true;
//    }
//
//}
