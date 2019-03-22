package net.newglobe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.newglobe.model.User;
import net.newglobe.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	

	@RequestMapping("/user")
	public List<User> getUsers(){
		return userService.getUser();
	}
	
	@RequestMapping("/user1")
	public List<User> user1(){
		return userService.getUser();
	}
}