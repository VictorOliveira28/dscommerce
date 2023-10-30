package com.victoroliveira.dscommerce.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victoroliveira.dscommerce.dto.CategoryDTO;
import com.victoroliveira.dscommerce.entities.Category;
import com.victoroliveira.dscommerce.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;	
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {		
		List<Category> result = repository.findAll();			
		return result.stream().map(x -> new CategoryDTO(x)).toList();
		
	}	
	
}