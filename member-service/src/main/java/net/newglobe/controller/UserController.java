package net.newglobe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.newglobe.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	

	@RequestMapping("/user")
	public String getUsers(){
		return userService.getUser();
	}
}