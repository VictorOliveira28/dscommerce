package com.victoroliveira.dscommerce.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.victoroliveira.dscommerce.entities.User;
import com.victoroliveira.dscommerce.services.exceptions.ForbiddenException;
import com.victoroliveira.dscommerce.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

	@InjectMocks
	private AuthService service;
	
	@Mock
	private UserService userService;
	
	private User admin;	
	private User selfClient;
	private User otherClient;
	
	@BeforeEach
	void setUp() throws Exception{

		admin = UserFactory.createAdminUser();
		selfClient = UserFactory.createCustomClientUser(1L, "Bob");
		otherClient = UserFactory.createCustomClientUser(3L, "Ana");
		
	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenAdminLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(admin);
		
		Long userId = admin.getId();
		
		Assertions.assertDoesNotThrow(() -> {
			
			service.validateSelfOrAdmin(userId);
			
		});
		
	}
	
	@Test
	public void validateSelfOrAdminShouldDoNothingWhenSelfLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId = selfClient.getId();
		
		Assertions.assertDoesNotThrow(() -> {
			
			service.validateSelfOrAdmin(userId);
		});

	}
	
	@Test
	public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenOtherClientLogged() {
		
		Mockito.when(userService.authenticated()).thenReturn(selfClient);
		
		Long userId = otherClient.getId();
		
		Assertions.assertThrows(ForbiddenException.class, () -> {			
			service.validateSelfOrAdmin(userId);
		});
	}
}
