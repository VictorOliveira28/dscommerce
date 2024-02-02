package com.victoroliveira.dscommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.victoroliveira.dscommerce.dto.UserDTO;
import com.victoroliveira.dscommerce.entities.User;
import com.victoroliveira.dscommerce.projections.UserDetailsProjection;
import com.victoroliveira.dscommerce.repositories.UserRepository;
import com.victoroliveira.dscommerce.tests.UserDetailsFactory;
import com.victoroliveira.dscommerce.tests.UserFactory;
import com.victoroliveira.dscommerce.util.CustomUserUtil;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
	
	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private CustomUserUtil userUtil;
	
	private String validUsername;
	private String invalidUsername;	
	private User user;	
	List<UserDetailsProjection> userDetails;
	
	@BeforeEach
	void setUp() {
		
		validUsername = "maria@gmail.com";
		invalidUsername = "cicrano@gmail.com";
		
		user = UserFactory.createCustomClientUser(1L, validUsername);		
		userDetails = UserDetailsFactory.createCustomClientUser(validUsername);
		
		
		Mockito.when(repository.searchUserAndRolesByEmail(validUsername)).thenReturn(userDetails);
		Mockito.when(repository.searchUserAndRolesByEmail(invalidUsername)).thenReturn(new ArrayList<>());
		
		Mockito.when(repository.findByEmail(validUsername)).thenReturn(Optional.of(user));
		Mockito.when(repository.findByEmail(invalidUsername)).thenReturn(Optional.empty());
	
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
	
	@Test
	public void authenticatedShouldReturnUserWhenUserExists() {
		
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(validUsername);
		
		User result = service.authenticated();
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getUsername(), validUsername);
		
	}
	
	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenInvalidUsername() {
		
		Mockito.doThrow(ClassCastException.class).when(userUtil).getLoggedUsername();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			
			service.authenticated();
			
		});
		
	}
	
	@Test
	public void getMeShouldReturnUserDTOWhenUserAuthenticated() {
		
		UserService spyUserService = Mockito.spy(service);
		Mockito.doReturn(user).when(spyUserService).authenticated();		
		
		UserDTO dto = spyUserService.getMe();
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getEmail(), validUsername);
	}
	
	@Test
	public void getMeShouldThrowUsernameNotFoundExceptionWhenUserNotAuthenticated() {
		
		UserService spyUserService = Mockito.spy(service);
		Mockito.doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> {
			
			@SuppressWarnings("unused")
			UserDTO result = spyUserService.getMe();
			
		});
		
	}

}
