package net.newglobe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

	@Autowired
	private RestTemplate restTemplate;

//	@Autowired
//	private LoadBalancerClient loadBalancerClient;//ribbon负载均衡器

//	@RequestMapping("/test")
//	public SysAccount[] getUsers() {
//		Date date1 = new Date();
//		SysAccount[] users = restTemplate.getForObject("http://memberService/SysAccount", SysAccount[].class);
//
//		Date date2 = new Date();
//		System.out.println(date2.getTime() - date1.getTime());
//		return users;
//	}
//
//	@RequestMapping("/test1")
//	public List<SysAccount> getUsers1() {
//		Date date1 = new Date();
//		List<SysAccount> users = restTemplate.getForObject("http://memberService/user1", List.class);
//		Date date2 = new Date();
//		System.out.println(date2.getTime() - date1.getTime());
//		return users;
//	}

}