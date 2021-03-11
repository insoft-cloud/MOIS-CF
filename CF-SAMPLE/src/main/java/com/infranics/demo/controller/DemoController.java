package com.infranics.demo.controller;

import com.infranics.demo.cloudfoundry.model.JpaScreeningClinic;
import com.infranics.demo.service.DemoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

@RestController
public class DemoController {

	final
	DemoService demoService;

	public DemoController(DemoService demoService) {
		this.demoService = demoService;
	}


	@GetMapping(value="/")
	public ModelAndView home(Model model) {
		ModelAndView mv = new ModelAndView("index");
		List<JSONArray> list = new ArrayList<JSONArray>();
		//list.add(getHosp());
		mv.addObject("list", getHosp());
		
		return mv;
	}

//	@GetMapping(value="/storage")
//	public ModelAndView storage(Model model) {
//		ModelAndView mv = new ModelAndView("user-provide");
//		List<JSONArray> list = new ArrayList<JSONArray>();
//		//list.add(getHosp());
//		mv.addObject("list", getHosp());
//
//		return mv;
//	}
	
	public List<JpaScreeningClinic> getHosp() {
		return demoService.ListData();
	}
}
