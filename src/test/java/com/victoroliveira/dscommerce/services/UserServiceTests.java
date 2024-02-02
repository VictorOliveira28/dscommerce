package com.victoroliveira.dscommerce.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.victoroliveira.dscommerce.entities.User;
import com.victoroliveira.dscommerce.projections.UserDetailsProjection;
import com.victoroliveira.dscommerce.repositories.UserRepository;
import com.victoroliveira.dscommerce.tests.UserDetailsFactory;
import com.victoroliveira.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	private String validUsername;
	private String invalidUsername;	
	private User user;
	List<UserDetailsProjection> userDetails;
	
	@BeforeEach
	void setUp() {
		
		validUsername = "fulanodetal@gmail.com";
		invalidUsername = "cicrano@gmail.com";
		
		user = UserFactory.createCustomClientUser(1L, validUsername);
		userDetails = UserDetailsFactory.createCustomClientUser(validUsername);
		
		
		Mockito.when(repository.searchUserAndRolesByEmail(validUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(invalidUsername)).thenReturn(new ArrayList<>());

	}
	
	@Test
	public void loadByUsernameShouldReturnUserDetailsWhenValidUsername() {
		
		UserDetails result = service.loadUserByUsername(validUsername);
		
		Assertions.assertNotNull(result);
		
		Assertions.assertEquals(result.getUsername(), validUsername);
	}
	
	@Test
	public void loadByUsernameShouldThrowUsernameNotFoundExceptionWhenInvalidUsername() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, () ->{
			
			service.loadUserByUsername(invalidUsername);
			
		});
	}

}
