package net.newglobe.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	public Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	
	@RequestMapping({"index",""})
	public String index(HttpServletRequest request) {
		//SysAccount sysAccount = (SysAccount) SecurityUtils.getSubject().getPrincipal();
		return "index";
	}
	
}
