package org.openpaas.paasta.portal.web.admin.controller;

import org.apache.tomcat.util.bcel.Const;
import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.Constants;
import org.openpaas.paasta.portal.web.admin.common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 도메인 정보 조회, 추가, 삭제 등 도메인 관리의 API 를 호출 받는 컨트롤러이다.
 *
 * @version 1.0
 * @since 2016 -09-06
 */
@RestController
public class OrgSpaceListController extends Common {

    private final String V2_URL = "/v2";

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgSpaceListController.class);

    /**
     * Gets org space list main.
     *
     * @return the org space list main
     */
    @GetMapping("/orgspacelist")
    public ModelAndView getOrgSpaceListMain() {
        return orgSpaceListService.getOrgSpaceListMain();
    }


    //----------------------------------------------------------------------------------------------------------//

    /**
     * 조직 정보를 조회한다.
     *
     * @return Map
     */
    @GetMapping(value = {Constants.V2_URL + "/orgs/{orgId}"})
    @ResponseBody
    public Map<String, Object> getOrg(@PathVariable String orgId, HttpServletRequest request) {
        return commonService.procApiRestTemplate(Constants.V3_URL + "/orgs/" + orgId, HttpMethod.GET, null, Constants.CF_API);
    }

    /**
     * admin 유저로 접근 가능한 조직 목록(모든 조직 목록)을 조회한다.
     *
     * @return Map (자바 Map 클래스)
     * @throws Exception the exception
     * @thorws Exception
     */
//    @GetMapping(V2_URL + "/orgs")
//    public Map<String, Object> getOrgsForAdmin( HttpServletRequest request) throws Exception {
//        String key = request.getParameter("key");
//        LOGGER.info("!!!!!!!!!!!!!!!!!!! "  +key);
//        return orgSpaceListService.getOrgsForAdmin("/orgs-admin", HttpMethod.GET, null, getToken());
//    }
    @GetMapping(V2_URL + "/orgs")
    public Map<String, Object> getOrgsForAdmin(HttpServletResponse response, HttpServletRequest request) throws Exception {
        return orgSpaceListService.getOrgsForAdmin("/orgs", HttpMethod.GET, null, response, request);
    }


    /**
     * admin 유저로 접근 가능한 영역 목록(모든 영역 목록)을 조회한다.
     *
     * @param orgid org id
     * @return Map (자바 Map 클래스)
     * @throws Exception the exception
     */
    @GetMapping(V2_URL + "/orgs/{orgid}/spaces")
    public Map<String, Object> getSpacesForAdmin(@PathVariable String orgid, HttpServletRequest request) throws Exception {
        return orgSpaceListService.getSpacesForAdmin("/orgs/" + orgid + "/spaces-admin", HttpMethod.GET, null);
    }

    /**
     * 조직 요약 정보를 조회한다.
     *
     * @param orgid org id
     * @return Org 조직 객체
     */
    @GetMapping(V2_URL + "/orgs/{orgid}/summary")
    public Map<String, Object> getOrgSummary(@PathVariable String orgid, HttpServletRequest request) {
        return orgSpaceListService.getOrgSummary("/orgs/" + orgid + "/summary-admin", HttpMethod.GET, null);
    }


    /**
     * 조직 이름으로 조직의 정보를 조회한다.
     *
     * @param orgid orgid
     * @return Org 조직 객체
     */
    @GetMapping(V2_URL + "/orgs/{orgid}/quota")
    public Map<String, Object> getOrgQuota(@PathVariable String orgid, HttpServletRequest request) {
        return orgSpaceListService.getOrgQuota("/orgs/" + orgid + "/quota-admin", HttpMethod.GET, null);
    }


    /**
     * 영역 요약 정보를 조회한다.
     *
     * @param spaceid space id값
     * @return Space 영역 객체
     */
    @GetMapping(V2_URL + "/spaces/{spaceid}/summary")
    public Map<String, Object> getSpaceSummary(@PathVariable String spaceid, HttpServletRequest request) {
        return orgSpaceListService.getSpaceSummary("/spaces/" + spaceid + "/summary-admin", HttpMethod.GET, null);
    }

    /**
     * 영역 쿼터 정보를 조회한다.
     *
     * @param spacequtaid spacequta id
     * @return Map (자바 Map 클래스)
     */
    @GetMapping(V2_URL + "/spaces/{spacequtaid}/quota")
    public Map<String, Object> getSpaceQuota(@PathVariable String spacequtaid, HttpServletRequest request) {
        return orgSpaceListService.getSpaceQuota("/spaces/" + spacequtaid + "/quota-admin", HttpMethod.GET, null);
    }

    /**
     * 특정 영역을 선택하여 영역을 조회한다.
     *
     * @return Map (자바 Map 클래스)
     */
    @GetMapping(V2_URL + "/spaces")
    public Map<String, Object> getSpace(HttpServletRequest request) {
        return orgSpaceListService.getSpace("/spaces-admin", HttpMethod.GET, null);

    }
}
