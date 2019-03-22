package net.newglobe.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import net.newglobe.model.User;

@Service
public class UserService {
	
	public List<User> getUser() {
		List<User> list = new LinkedList<User>();
		for(int i=0; i<5; i++) {
			User u = new User();
			u.setId(1L);
			u.setName("张"+i);
			u.setAge(i);
			list.add(u);
		}
		return list;
	}

}
