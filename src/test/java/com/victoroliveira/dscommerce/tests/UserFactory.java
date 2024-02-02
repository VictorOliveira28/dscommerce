package com.victoroliveira.dscommerce.tests;

import java.time.LocalDate;

import com.victoroliveira.dscommerce.entities.Role;
import com.victoroliveira.dscommerce.entities.User;

public class UserFactory {
	
	public static User createClientUser() {	
		
		User user = new User(1L, "Maria", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"),"$2a$10$zhEDObSJaJjFoT2N6NXsjO.Q7jJtYEVMHW6Q35b2nFu7B4IgXQjoC");		
		user.addRole(new Role(1L, "ROLE_CLIENT"));	
		
		return user;
		
	}
	
	public static User createAdminUser() {
			
		User user = new User(2L, "Bob", "bob@gmail.com", "988835798", LocalDate.parse("2000-03-18"),"$2a$10$zhEDObSJaJjFoT2N6NXsjO.Q7jJtYEVMHW6Q35b2nFu7B4IgXQjoC");		
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		
		return user;			
		
	}
	
	public static User createCustomAdminUser(Long id, String username) {
		
		User user = new User(id, username, "bob@gmail.com", "988835798", LocalDate.parse("2000-03-18"),"$2a$10$zhEDObSJaJjFoT2N6NXsjO.Q7jJtYEVMHW6Q35b2nFu7B4IgXQjoC");		
		user.addRole(new Role(2L, "ROLE_ADMIN"));
		
		return user;
	}
	
public static User createCustomClientUser(Long id, String username) {
		
		User user = new User(id, username, "maria@gmail.com", "988835798", LocalDate.parse("2000-03-18"),"$2a$10$zhEDObSJaJjFoT2N6NXsjO.Q7jJtYEVMHW6Q35b2nFu7B4IgXQjoC");		
		user.addRole(new Role(1L, "ROLE_CLIENT"));
		
		return user;
	}

}
