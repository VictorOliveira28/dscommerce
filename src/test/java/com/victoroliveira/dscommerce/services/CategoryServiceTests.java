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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.victoroliveira.dscommerce.dto.CategoryDTO;
import com.victoroliveira.dscommerce.entities.Category;
import com.victoroliveira.dscommerce.repositories.CategoryRepository;
import com.victoroliveira.dscommerce.tests.CategoryFactory;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {
	
	@InjectMocks
	private CategoryService service;
	
	@Mock
	private CategoryRepository repository;
	
	private Category category;
	private List<Category> list;
	
	@BeforeEach
	void setUp() throws Exception {
	
		category = CategoryFactory.createCategory();
		
		list = new ArrayList<>();
		list.add(category);
		
		Mockito.when(repository.findAll()).thenReturn(list);

	}
	
	@Test
	public void findAllShouldReturnListCategoryDTO() {
		
		List<CategoryDTO> result = service.findAll();
		
		Assertions.assertEquals(result.size(), 1);
		Assertions.assertEquals(result.get(0).getId(), category.getId());
		Assertions.assertEquals(result.get(0).getName(), category.getName());
		
	}	

}