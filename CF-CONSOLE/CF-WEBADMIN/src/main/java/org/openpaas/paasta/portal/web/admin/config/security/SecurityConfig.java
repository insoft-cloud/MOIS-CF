package org.openpaas.paasta.portal.web.admin.config.security;
//
//import org.openpaas.paasta.portal.web.admin.config.SSLUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import javax.servlet.http.HttpServletRequest;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Collections;
//
///**
// * The type Security config.
// */
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//    /**
//     * Configure global.
//     *
//     * @param authenticationMgr the authentication mgr
//     * @throws Exception the exception
//     */
//
//    @Value("${cf.logoutRedirectUrl}")
//    private String logoutRedirectUrl;
//
//    @Autowired
//    CustomLogoutHandler customLogoutHandler;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
//
//
//        authenticationMgr.authenticationProvider(customAuthenticationProvider());
//
//
//    }
//
//    @Bean
//    AuthenticationProvider customAuthenticationProvider() throws KeyManagementException, NoSuchAlgorithmException {
//        //SSLUtils.turnOffSslChecking();
//        CustomAuthenticationProvider impl = new CustomAuthenticationProvider();
//        return impl;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/index").permitAll()
//                .antMatchers("/error").permitAll()
//                .antMatchers("/login/**").permitAll()
//                .antMatchers("/callback").permitAll()
//                .antMatchers("/resources/**").permitAll()
//                .antMatchers( "/favicon.ico").permitAll()
//                .antMatchers("/**").access("hasAnyRole('USER, ADMIN')")
//                .and()
//                .formLogin().loginPage("/index")
//                .defaultSuccessUrl("/dashboard")
//                //.failureUrl("/error")
//                .usernameParameter("id").passwordParameter("password")
//                .and()
//                .logout().addLogoutHandler(customLogoutHandler);
//    }
//
//
//    private CorsConfigurationSource corsConfiguration(){
//        return new CorsConfigurationSource() {
//            @Override
//            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                CorsConfiguration config = new CorsConfiguration();
//                config.setAllowedHeaders(Collections.singletonList("*"));
//                config.setAllowedMethods(Collections.singletonList("*"));
//                config.addAllowedOrigin("*");
//                config.setAllowCredentials(true);
//                return config;
//            }
//        };
//    }
//
//
//}
////Spring boot Admin 정보 접근 URL -  시작
//
////                        .antMatchers("/info**").permitAll()
////                        .antMatchers("/env**").permitAll()
////                        .antMatchers("/metrics**").permitAll()
////                        .antMatchers("/trace**").permitAll()
////                        .antMatchers("/dump**").permitAll()
////                        .antMatchers("/jolokia**").permitAll()
////                        .antMatchers("/configprops**").permitAll()
////                        .antMatchers("/logfile**").permitAll()
////                        .antMatchers("/logging**").permitAll()
////                        .antMatchers("/refresh**").permitAll()
////                        .antMatchers("/flyway**").permitAll()
////                        .antMatchers("/liquibase**").permitAll()
////                        .antMatchers("/httptrace**").permitAll()
////                        .antMatchers("/threaddump**").permitAll()
////                        .antMatchers("/heapdump**").permitAll()
////                        .antMatchers("/loggers**").permitAll()
////                        .antMatchers("/auditevents**").permitAll()
////                        .antMatchers("/hystrix.stream**").permitAll()
////                        .antMatchers("/docs**").permitAll()
////                        .antMatchers("/jmx**").permitAll()
////                        .antMatchers("/management/**").permitAll()
////                        .antMatchers("/applications/**").permitAll()
////                        .antMatchers("/applications/**/**").permitAll()
////                        .antMatchers("/applications/**/**/**").permitAll()
////                        .antMatchers("/health**").permitAll()
////                        .antMatchers("/health/**").permitAll()
////Spring boot Admin 정보 접근 URL - 끝

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     *
     * Since Spring Security requires that role names start with "ROLE_",
     * a SimpleAuthorityMapper is used to instruct the KeycloakAuthenticationProvider
     * to insert the "ROLE_" prefix.
     *
     * e.g. Librarian -> ROLE_Librarian
     *
     * Should you prefer to have the role all in uppercase, you can instruct
     * the SimpleAuthorityMapper to convert it by calling:
     * {@code grantedAuthorityMapper.setConvertToUpperCase(true); }.
     * The result will be: Librarian -> ROLE_LIBRARIAN.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    /**
     * Defines the session authentication strategy.
     *
     * RegisterSessionAuthenticationStrategy is used because this is a public application
     * from the Keycloak point of view.
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Define an HttpSessionManager bean only if missing.
     *
     * This is necessary because since Spring Boot 2.1.0, spring.main.allow-bean-definition-overriding
     * is disabled by default.
     */
    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    /**
     * Define security constraints for the application resources.
     */



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.httpBasic().disable().csrf().disable().cors().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/sso/login").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/callback").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers( "/favicon.ico").permitAll()
                .antMatchers("/**").authenticated();
        http.logout().logoutSuccessUrl("https://admin.dev.egovp.kr/eGovPlatform/admin/app/login");
    }
}
