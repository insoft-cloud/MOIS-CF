package org.openpaas.paasta.portal.web.admin.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public ModelAndView errorPage(HttpServletRequest request) {
        ModelAndView mv =new ModelAndView();
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                mv.setViewName("/404");
                return mv;
            }
        }
        mv.setViewName("/404");
        return mv;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
