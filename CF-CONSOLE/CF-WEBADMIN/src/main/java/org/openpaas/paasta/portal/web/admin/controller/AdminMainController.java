package org.openpaas.paasta.portal.web.admin.controller;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 운영자 포탈 관리자 대시보드 관련 API 를 호출 하는 컨트롤러이다.
 *
 * @version 1.0
 * @since 2016.09.08 최초작성
 */
@RestController
public class AdminMainController extends Common {

    private final String V2_URL = "/v2";

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMainController.class);

    @Autowired
    HttpServletRequest request;

//    /**
//     * 관리자포탈 조직 선택 메인페이지로 이동한다.
//     *
//     * @param organizationId 조직 아이디(String)
//     * @return ModelAndView(Spring 클래스)
//     */
//    @GetMapping(value = {V2_URL + "/statistics/organizations/{organizationId}"})
//    public ModelAndView getAdminMain(@PathVariable("organizationId") String organizationId) {
//        return new ModelAndView() {{
//            setViewName("/main/main");
//            addObject("ORGANIZATION_ID", organizationId);
//        }};
//    }


    /**
     * 전체 조직 수, 영역 수, APPLICATION 수, 사용자 수 목록을 조회한다.
     *
//     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    @GetMapping(value = {V2_URL + "/statistics"})
    public Map<String, Object> getTotalCountList() {

        //return commonService.procApiRestTemplate(V2_URL + "/statistics", HttpMethod.GET, null, Constants.COMMON_API);
        return commonService.procApiRestTemplate(V2_URL + "/statistics/"+request.getSession().getAttribute("userID").toString(), HttpMethod.GET, null, Constants.COMMON_API);
    }

    private KeycloakSecurityContext getKeycloakSecurityContext(HttpServletRequest request) {
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
    /**
     * 전체 조직 통계 목록을 조회한다.
     * *
     *
//     * @param param AdminMain(모델클래스)
     * @return Map(자바클래스)
     */
    @GetMapping(value = {V2_URL + "/statistics/organizations"})
    public Map<String, Object> getTotalOrganizationList() {
        return commonService.procApiRestTemplate(V2_URL + "/statistics/organizations/"+request.getSession().getAttribute("userID").toString(), HttpMethod.GET, null,Constants.COMMON_API);
    }


    /**
     * 해당 조직에 대한 공간들의 통계 목록들을 조회한다.
     * *
     * @return Map(자바클래스)
     */
    @GetMapping(value = {V2_URL + "/statistics/organizations/{organizationId}/spaces"})
    public Map<String, Object> getTotalSpaceList(@PathVariable String organizationId) {
        return commonService.procApiRestTemplate(V2_URL + "/statistics/organizations/"+organizationId+"/spaces", HttpMethod.GET, null,Constants.COMMON_API);
    }

}
