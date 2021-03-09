package org.openpaas.paasta.portal.web.admin.controller;

import org.openpaas.paasta.portal.web.admin.common.Common;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

@Controller
public class DashboardController extends Common {

    /**
     * 로그인처리 후 첫 화면
     *
     * @return ModelAndView model
     */
    @RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();

        if (!request.isUserInRole("ROLE_ADMIN")) {
            mv.setViewName("redirect:/index");
        } else {
            mv.setViewName("/main/main");
        }

        return mv;
    }


}
