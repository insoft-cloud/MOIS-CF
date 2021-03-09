package org.openpaas.paasta.portal.web.admin.service;

import com.sun.org.apache.bcel.internal.generic.SWITCH;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.jetty.core.AbstractKeycloakJettyAuthenticator;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.Constants;
import org.openpaas.paasta.portal.web.admin.common.User;
import org.openpaas.paasta.portal.web.admin.common.UserList;
import org.openpaas.paasta.portal.web.admin.config.SSLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * 공통 기능을 구현한 서비스 클래스이다.
 *
 * @author 박철한
 * @version 2.0
 * @since 2020.05.25 업데이트
 */
@Service
public class CommonService extends Common {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
    private static final String LOGIN_HEADER_KEY = "Content-Type";
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CF_AUTHORIZATION_HEADER_KEY = "cf-Authorization";
//    private final RestTemplate restTemplate;


    @Autowired
    HttpServletRequest request;

    RestTemplate restTemplate;


    private String apiUrl;

    @Value("${paasta.portal.api.authorization.base64}")
    private String base64Authorization;

    @Value("${paasta.portal.api.zuulUrl.cfapi}")
    private String cfApiUrl;

    @Value("${cf.uaaDomain}")
    private String domain;

    @Value("${cf.logoutRedirectUrl}")
    private String logoutRedirectUrl;

    @Value("${cf.portalClient}")
    private String portalClient;

    @Value("${cf.clientSecret}")
    private String clientSecret;

    @Value("${paasta.portal.api.zuulUrl.commonapi}")
    private String commonApiUrl;

    @Value("${paasta.portal.api.zuulUrl.storageapi}")
    private String storageApiUrl;

    @Value("${paasta.portal.storageapi.type}")
    private String storageApiType;

    /**
     * REST TEMPLATE 처리
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param obj        the obj
     * @param reqToken   the req token
     * @return map map
     */
    public Map<String, Object> procRestTemplate(String reqUrl, HttpMethod httpMethod, Object obj, String reqToken) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);

        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);
        ResponseEntity<Map> resEntity = restTemplate.exchange(apiUrl + reqUrl, httpMethod, reqEntity, Map.class);
        Map<String, Object> resultMap = resEntity.getBody();



        return resultMap;
    }
    /**
     * REST TEMPLATE 처리
     *
     * @param reqUrl   the req url
     * @param file     the file
     * @param reqToken the req token
     * @return map map
     * @throws Exception the exception
     */
    public Map<String, Object> procRestTemplate(String reqUrl, MultipartFile file, String reqToken) throws Exception {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);

        final MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>();

        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() throws IllegalStateException {
                return file.getOriginalFilename();
            }
        };

        data.add("file", resource);
        final HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(data, reqHeaders);
        final ResponseEntity<Map> resEntity = restTemplate.exchange(apiUrl + reqUrl, HttpMethod.POST, requestEntity, Map.class);

        Map resultMap = resEntity.getBody();


        return resultMap;
    }


    /**
     * REST TEMPLATE 처리
     *
     * @param <T>          the type parameter
     * @param reqUrl       the req url
     * @param httpMethod   the http method
     * @param obj          the obj
     * @param reqToken     the req token
     * @param responseType the response type
     * @return response entity
     */
    public <T> ResponseEntity<T> procRestTemplate(String reqUrl, HttpMethod httpMethod, Object obj, String reqToken, Class<T> responseType) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);

        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);

        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);
        ResponseEntity<T> result = restTemplate.exchange(apiUrl + reqUrl, httpMethod, reqEntity, responseType);

        return result;
    }

    /**
     * REST TEMPLATE 처리
     *
     * @param <T>          the type parameter
     * @param reqUrl       the req url
     * @param httpMethod   the http method
     * @param obj          the obj
     * @param responseType the response type
     * @return response entity
     */
    public <T> ResponseEntity<T> procRestTemplateV2(String reqUrl, HttpMethod httpMethod, Object obj, String reqToken, Class<T> responseType) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);

        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);

        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);

        ResponseEntity<T> result = restTemplate.exchange(apiUrl + reqUrl, httpMethod, reqEntity, responseType);

        return result;
    }

    /**
     * REST TEMPLATE 처리
     *
     * @param <T>          the type parameter
     * @param reqUrl       the req url
     * @param httpMethod   the http method
     * @param obj          the obj
     * @param responseType the response type
     * @return response entity
     */
    public <T> ResponseEntity<T> procRestTemplateV2(String reqUrl, HttpMethod httpMethod, Object obj, Class<T> responseType) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);

        ResponseEntity<T> result = restTemplate.exchange(apiUrl + reqUrl, httpMethod, reqEntity, responseType);

        return result;
    }


    /**
     * USER ID를 조회한다.
     *
     * @return user id
     */
    public String getUserId() {

//        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken)request.getUserPrincipal();
//        LOGGER.info("HERE");
//
//        LOGGER.info(token.getName());
//        KeycloakPrincipal principal = (KeycloakPrincipal)token.getPrincipal();
//        LOGGER.info(principal.getName());
//        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "";
    }


    /**
     * USER ID를 설정한다.
     *
     * @param classObject the class object
     * @return the object
     * @throws Exception the exception
     */
    public Object setUserId(Object classObject) throws Exception {
        String methodName = "setUserId";
        Method method = classObject.getClass().getMethod(methodName, String.class);
        Object paramObject = this.getUserId() ;

        method.invoke(classObject, paramObject);

        return classObject;
    }


    /**
     * REST TEMPLATE 처리 - CfApi(PortalApi)
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param obj        the obj
     * @param reqToken   the req token
     * @return map map
     */
    public Map<String, Object> procCfApiRestTemplate(String reqUrl, HttpMethod httpMethod, Object obj, String reqToken) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);

        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //reqToken = user.getToken();
        reqToken = "";
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);
        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);

        ResponseEntity<Map> resEntity = restTemplate.exchange(cfApiUrl + reqUrl, httpMethod, reqEntity, Map.class);
        Map<String, Object> resultMap = resEntity.getBody();

        if (resultMap != null) {

        }
        return resultMap;
    }

    /**
     * REST TEMPLATE 처리 - CommonApi
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param obj        the obj
     * @param reqToken   the req token
     * @return map map
     */
    public Map<String, Object> procCommonApiRestTemplate(String reqUrl, HttpMethod httpMethod, Object obj, String reqToken) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reqToken = "";
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);


        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);
        ResponseEntity<Map> resEntity = restTemplate.exchange(commonApiUrl + reqUrl, httpMethod, reqEntity, Map.class);
        Map<String, Object> resultMap = resEntity.getBody();

        LOGGER.info("procCommonApiRestTemplate reqUrl :: {} || status code :: {}", reqUrl, resEntity.getStatusCode());
        return resultMap;
    }


    /**
     * REST TEMPLATE 처리 - CommonApi
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param obj        the obj
     * @param reqToken   the req token
     * @return map map
     */
    public Map<String, Object> procCommonApiRestTemplate(String apiUri, String reqUrl, String authorization, HttpMethod httpMethod, Object obj, String reqToken) {
        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, authorization);
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reqToken="";
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);

        //LOGGER.info("CommonApiUrl::" + apiUri + "/commonapi" + reqUrl);
        HttpEntity<Object> reqEntity = new HttpEntity<>(obj, reqHeaders);
        ResponseEntity<Map> resEntity = restTemplate.exchange(apiUri + "/commonapi" + reqUrl, httpMethod, reqEntity, Map.class);
        Map<String, Object> resultMap = resEntity.getBody();

        //LOGGER.info("procCommonApiRestTemplate reqUrl :: {} || status code :: {}", reqUrl, resEntity.getStatusCode());
        return resultMap;
    }

    /**
     * REST TEMPLATE 처리 - StorageApi
     *
     * @param reqUrl     the req url
     * @param httpMethod the http method
     * @param bodyObject the obj
     * @param reqToken   the req token
     * @return map map
     */
    public <T> ResponseEntity<T> procStorageApiRestTemplate(String reqUrl, HttpMethod httpMethod, Object bodyObject, String reqToken, Class<T> resClazz) {
        restTemplate = new RestTemplate();

        // create url
        String storageRequestURL = storageApiUrl + "/v2/" + storageApiType + '/';
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reqToken = "";
        if (null != reqUrl && false == "".equals(reqUrl)) storageRequestURL += reqUrl;

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
        if (null != reqToken && !"".equals(reqToken)) reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, reqToken);
        if (null == bodyObject) bodyObject = new LinkedMultiValueMap<>();
        HttpEntity<Object> reqEntity = new HttpEntity<>(bodyObject, reqHeaders);


        ResponseEntity<T> resEntity = restTemplate.exchange(storageRequestURL, httpMethod, reqEntity, resClazz);
       // LOGGER.info("procRestStorageApiTemplate reqUrl :: {} || resultEntity type :: {}", storageRequestURL, resEntity.getHeaders().getContentType());
        //LOGGER.info("procRestStorageApiTemplate response Http status code :: {}", resEntity.getStatusCode());
        return resEntity;
    }

    public ResponseEntity<String> procStorageApiRestTemplateText(String reqUrl, HttpMethod httpMethod, Object bodyObject, String reqToken) {
        //LOGGER.info(">>> Init procStorageApiRestTemplateText >> reqUrl "+reqUrl +" :: httpMethod "+ httpMethod+" :: "+ "reqToken"+ reqToken);
        return procStorageApiRestTemplate(reqUrl, httpMethod, bodyObject, reqToken, String.class);
    }

    public ResponseEntity<byte[]> procStorageApiRestTemplateBinary(String reqUrl, HttpMethod httpMethod, Object bodyObject, String reqToken) {
        //LOGGER.info(">>> Init procStorageApiRestTemplateBinary >> reqUrl " + reqUrl + " :: httpMethod" + httpMethod + " :: " + bodyObject + " :: " + "reqToken" + reqToken);
        return procStorageApiRestTemplate(reqUrl, httpMethod, bodyObject, reqToken, byte[].class);
    }

    /**
     * REST TEMPLATE 처리 - GerUser
     *
     * @return map map
     */
    public Map<String, Object> procGetUserRestTemplate(String token) {

        restTemplate = new RestTemplate();
        HttpHeaders reqHeaders = new HttpHeaders();
        String bearerToken = "Bearer " + token;
        reqHeaders.add(AUTHORIZATION_HEADER_KEY, bearerToken);
        //LOGGER.info(bearerToken);

        HttpEntity<Object> reqEntity = new HttpEntity<>(null, reqHeaders);
        ResponseEntity<Map> resEntity = restTemplate.exchange(domain + "/userinfo", HttpMethod.GET, reqEntity, Map.class);
        Map<String, Object> resultMap = resEntity.getBody();

        //LOGGER.info("> resEntity : " +resEntity);

        if (resultMap != null) {
            //LOGGER.info("procCfApiRestTemplate reqUrl :: {} ", resultMap);
        }
        return resultMap;
    }

    /**
     * REST TEMPLATE 처리 - LOGOUT
     *
     * @return map map
     */
    public Map<String, Object> procLogOutRestTemplate(String endpoint, HttpHeaders httpHeaders ,HttpMethod httpMethod, Object param) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            restTemplate = new RestTemplate();
            HttpEntity<Object> reqEntity = new HttpEntity<>(param, httpHeaders);
            //LOGGER.info(endpoint);
            //LOGGER.info(reqEntity.toString());
            ResponseEntity<Map> resEntity = restTemplate.exchange(domain + endpoint, httpMethod, reqEntity, Map.class);
            //LOGGER.info("resEntity::" + resEntity.getBody());
            resultMap.put("result", true);
            resultMap.put("data", resEntity.getBody());
        } catch (Exception e){
            LOGGER.error(e.toString());
            resultMap.put("result", e.getMessage());
        }
        return resultMap;
    }



    public Map<String, Object> procApiRestTemplate(String endpoint, HttpMethod httpMethod, Object param, String api) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            restTemplate = new RestTemplate();
            HttpHeaders reqHeaders = new HttpHeaders();
            StringBuffer req = new StringBuffer();
            switch(api){
                case Constants.CF_API :
                case Constants.COMMON_API :
                case Constants.STORAGE_API :
                    switch (api){
                        case Constants.CF_API :  req.append(cfApiUrl + endpoint); break;
                        case Constants.COMMON_API :  req.append(commonApiUrl + endpoint); break;
                        case Constants.STORAGE_API :  req.append(storageApiUrl + endpoint); break;
                    }
                    reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
                    reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, getTokens());
                    break;
                case Constants.LOGOUT :
                case Constants.UAA_API :
                    req.append(domain + endpoint);
                    reqHeaders.add("Content-Type", "application/x-www-form-urlencoded");
                    reqHeaders.add("Accept", "application/json");
                    switch (api){
                        case Constants.LOGOUT :  reqHeaders.add("Authorization", this.getAdminPortalClientBase64Encoding()); break;
                        case Constants.UAA_API :  reqHeaders.add("Authorization", this.getUserBearerToken());break;
                    }
                    break;
                default: new Throwable("API 구문 오류");
            }
            HttpEntity<Object> reqEntity = new HttpEntity<>(param, reqHeaders);
            Map resEntity = restTemplate.exchange(req.toString(), httpMethod, reqEntity, Map.class).getBody();
            //LOGGER.info("resEntity::" + resEntity);
            if(resEntity.containsKey("status")){
                if(resEntity.containsKey("message")){
                    throw new Exception("status  :  " + resEntity.get("status") + " ,message : " + resEntity.get("message"));
                }
                throw new Exception("status  :  " + resEntity.get("status") + " ,message : Not Found Message");
            }
            resultMap.put("result", true);
            resultMap.put("data", resEntity);
        } catch (Exception e){
            LOGGER.error(e.toString());
            resultMap.put("result", false);
            resultMap.put("data", e.getMessage());
        }
        return resultMap;
    }



    public String getPortalClient() {
        return this.portalClient;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getLogoutRedirectUrl(){
        return this.logoutRedirectUrl;
    }

    private String getAdminPortalClientBase64Encoding() throws UnsupportedEncodingException {
        return "Basic " + Base64.getEncoder().encodeToString((this.getPortalClient() + ":" + this.getClientSecret()).getBytes("UTF-8"));
    }

    private String getUserBearerToken(){
//        LOGGER.info("HERE1");
//        //KeycloakAuthenticationToken token = (KeycloakAuthenticationToken)request.getUserPrincipal();
//        LOGGER.info(token.getName());
//        KeycloakPrincipal principal = (KeycloakPrincipal)token.getPrincipal();
//        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        LOGGER.info(principal.getName());


        return "";
    }

    public String getTokens(){

        if(request.getSession().getAttribute("token") == null || Long.valueOf(request.getSession().getAttribute("expires_in").toString()) < System.currentTimeMillis()){
            KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
            String json = "{ \"userName\" : " + "\""+principal.getAccount().getKeycloakSecurityContext().getIdToken().getPreferredUsername()+"\""+ "}";
            restTemplate = new RestTemplate();
            HttpHeaders reqHeaders = new HttpHeaders();
            reqHeaders.add(AUTHORIZATION_HEADER_KEY, base64Authorization);
            reqHeaders.add(CF_AUTHORIZATION_HEADER_KEY, "");
            reqHeaders.add("Accept", "application/json");
            reqHeaders.add("Content-Type", "application/json");
            HttpEntity<Object> reqEntity = new HttpEntity<>(json, reqHeaders);
            Map resEntity = restTemplate.exchange(cfApiUrl + "/v3/user/keycloak", HttpMethod.POST, reqEntity, Map.class).getBody();
            request.getSession().setAttribute("token",((Map)resEntity.get("tokenData")).get("access_token"));
            Long i =  System.currentTimeMillis() + (Long.valueOf(((Map)resEntity.get("tokenData")).get("expires_in").toString()) * 1000);
            request.getSession().setAttribute("expires_in", i);
            request.getSession().setAttribute("userID",resEntity.get("userID"));
            request.getSession().setAttribute("userName",principal.getAccount().getKeycloakSecurityContext().getIdToken().getPreferredUsername());
        }
        return request.getSession().getAttribute("token").toString();
    }




}
