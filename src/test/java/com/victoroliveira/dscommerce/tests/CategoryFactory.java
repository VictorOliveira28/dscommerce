package com.victoroliveira.dscommerce.tests;

import com.victoroliveira.dscommerce.entities.Category;

public class CategoryFactory {
	
	public static Category createCategory() {
		
		return new Category(1L, "Games");
		
	}
	
	public static Category createCategory(Long id, String name) {
		
		return new Category(id, name);
		
	}

}
