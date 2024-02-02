package com.victoroliveira.dscommerce.tests;

import com.victoroliveira.dscommerce.entities.Role;

public class RoleFactory {
	
	public static Role createRole() {		
		
		return new Role(1L, "ROLE_ADMIN");
		
	}

}
