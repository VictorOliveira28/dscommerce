package com.victoroliveira.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victoroliveira.dscommerce.dto.ProductDTO;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		
		Optional<Product> result = repository.findById(id);
		Product product = result.get();		
		ProductDTO dto = new ProductDTO(product);
		
		return dto;
		
	}
}