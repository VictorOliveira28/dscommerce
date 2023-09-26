package com.victoroliveira.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.victoroliveira.dscommerce.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	

}
