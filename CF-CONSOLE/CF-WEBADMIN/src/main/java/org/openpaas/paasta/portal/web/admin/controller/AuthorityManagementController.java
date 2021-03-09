package org.openpaas.paasta.portal.web.admin.controller;

import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 권한 컨트롤러 - 권한그룹과 권한을 조회, 수정, 삭제한다.
 *
 * @version 1.0
 * @since 2016.09.19 최초작성
 */
@RestController
public class AuthorityManagementController extends Common{

    private final String V2_URL = "/v2";
    /**
     * Gets org space list main.
     *
     * @return the org space list main
     */
    @GetMapping("/authority")
    public ModelAndView getOrgSpaceListMain() {
        return authorityManagementService.getOrgSpaceListMain();
    }

    /**
     * Gets authority groups.
     *
     *
     * @return the authority groups
     * @throws Exception the exception
     */
    @GetMapping(V2_URL+"/authority")
    @ResponseBody
    public Map<?, Object> getAuthorityGroups(HttpServletRequest request) throws Exception {
        return authorityManagementService.getAuthorityGroups("/authority", HttpMethod.GET, null);
    }

    /**
     * Gets uaa user info.
     *
     * @return the uaa user info
     * @throws Exception the exception
     */
    @GetMapping(V2_URL+"/authority/group")
    @ResponseBody
    public Map<String, Object> getUaaUserInfo(HttpServletRequest request) throws Exception {
        //사용안함???
        return authorityManagementService.getUaaUserInfo("/authority/group", HttpMethod.GET, null);
    }

    /**
     * Create authority group map.
     *
     * @param param the param
     * @return the map
     * @throws Exception the exception
     */
    @PostMapping(V2_URL+"/authority/group")
    @ResponseBody
    public Map<String, Object> createAuthorityGroup(HttpServletRequest request, @RequestBody Map param) throws Exception {
        return authorityManagementService.createAuthorityGroup("/authority/group", HttpMethod.POST, param);
    }

    /**
     * Delete authority group map.
     * @param groupguid the group guid
     * @param param the param
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping(V2_URL+"/authority/group/{groupguid}")
    @ResponseBody
    public Map<String, Object> deleteAuthorityGroup(@PathVariable String groupguid, HttpServletRequest request, @RequestBody Map param) throws Exception {
        return authorityManagementService.deleteAuthorityGroup("/authority/group/" + groupguid, HttpMethod.DELETE, param);
    }

    /**
     * Add group members map.
     *
     * @param param the param
     * @return the map
     * @throws Exception the exception
     */
    @PostMapping(V2_URL+"/authority/member")
    @ResponseBody
    public Map<String, Object> addGroupMembers(HttpServletRequest request, @RequestBody Map param) throws Exception {
        return authorityManagementService.addGroupMembers("/authority/member", HttpMethod.POST, param);
    }

    /**
     * Delete group members map.
     *
     * @param param the param
     * @return the map
     * @throws Exception the exception
     */
    @DeleteMapping(V2_URL+"/authority/member/{groupguid}")
    @ResponseBody
    public Map<String, Object> deleteGroupMembers(@PathVariable String groupguid, HttpServletRequest request, @RequestBody Map param) throws Exception {
        return authorityManagementService.deleteGroupMembers("/authority/member/"+groupguid, HttpMethod.DELETE, param);
    }

    /**
     * Gets user name list.
     *
     * @return the user name list
     * @throws Exception the exception
     *
     */
    @GetMapping(V2_URL+"/users")
    @ResponseBody
    public Map<String, Object> getUserNameList(@RequestParam("index") String index) throws Exception {
        return authorityManagementService.getUserNameList("/users?index="+index, HttpMethod.GET, null);
    }
}
