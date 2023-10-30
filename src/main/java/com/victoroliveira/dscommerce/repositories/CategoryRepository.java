package com.victoroliveira.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victoroliveira.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	

}
