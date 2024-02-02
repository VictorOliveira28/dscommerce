package com.victoroliveira.dscommerce.tests;

import com.victoroliveira.dscommerce.entities.Category;
import com.victoroliveira.dscommerce.entities.Product;

public class ProductFactory {
	
	public static Product createProduct() {
		
		Category category = CategoryFactory.createCategory();	
		Product product = new Product(1L, "Red Dead Redemption 2", "GTA de Cavalo", 250.0, "https://www.ireddead.com/img/content/1802.jpg");		
		product.getCategories().add(category);
		
		return product;
		
	}
	
	public static Product createProduct(String name) {
			
		Product product = createProduct();
		product.setName(name);
		
		return product;		
		
	}

}
