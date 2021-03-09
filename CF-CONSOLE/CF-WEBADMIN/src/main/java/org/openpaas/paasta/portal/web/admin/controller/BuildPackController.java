package org.openpaas.paasta.portal.web.admin.controller;

import org.openpaas.paasta.portal.web.admin.common.Common;
import org.openpaas.paasta.portal.web.admin.common.Constants;
import org.openpaas.paasta.portal.web.admin.common.User;
import org.openpaas.paasta.portal.web.admin.model.BuildPack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 빌드팩 컨트롤러 - 빌드팩 정보를 조회, 수정한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@RestController
public class BuildPackController extends Common {

    private final String V2_URL = "/v2";

    /**
     * 빌드팩 화면
     *
     * @return model and view
     */
    @GetMapping(value = {"/buildpacks"})
    public ModelAndView webIdeUser() {
        ModelAndView mv = new ModelAndView();

        mv.setViewName("buildPack/buildPackMain");

        return mv;
    }


    /**
     * 빌드팩 조회
     *
     * @param buildPack the buildPack
     * @return String rspApp
     */
    @GetMapping(value = {V2_URL + "/buildpacks"})
    public Map<String, Object> getBuildPacks(HttpServletRequest request, @ModelAttribute BuildPack buildPack) {
        return commonService.procApiRestTemplate(Constants.V2_URL + "/buildpacks", HttpMethod.GET, buildPack,Constants.CF_API);

    }

    /**
     * 빌드팩 정보 수정
     *
     * @param buildPack the buildPack
     * @return String rspApp
     */
    @PutMapping(value = {V2_URL + "/buildpacks/{guid}"})
    public Map<String, Object> updateBuildPack(HttpServletRequest request, @RequestBody BuildPack buildPack, @PathVariable String guid) {
        buildPack.setGuid(UUID.fromString(guid));
        return commonService.procApiRestTemplate(Constants.V3_URL + "/buildpacks/" + buildPack.getGuid().toString(), HttpMethod.PUT, buildPack,Constants.CF_API);
    }
}
