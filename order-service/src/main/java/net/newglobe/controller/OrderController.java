package net.newglobe.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import net.newglobe.model.User;

@RestController
public class OrderController {
	
	 @Autowired
	 private RestTemplate restTemplate;
	
//	@Autowired
//	private LoadBalancerClient loadBalancerClient;//ribbon负载均衡器

	@RequestMapping("/test")
	public List<User> getUsers(){
		Date date1 = new Date();
		List<User> users = restTemplate.getForObject("http://memberService/user", List.class);
		Date date2 = new Date();
		System.out.println(date2.getTime()-date1.getTime());
		return users;
	}
	@RequestMapping("/test1")
	public List<User> getUsers1(){
		Date date1 = new Date();
		List<User> users = restTemplate.getForObject("http://memberService/user1", List.class);
		Date date2 = new Date();
		System.out.println(date2.getTime()-date1.getTime());
		return users;
	}
	
	
}