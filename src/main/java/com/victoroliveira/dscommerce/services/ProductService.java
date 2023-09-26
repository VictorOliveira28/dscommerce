package com.victoroliveira.dscommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {		
		Page<Product> result = repository.findAll(pageable);			
		return result.map(x -> new ProductDTO(x));
		
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {		
		
		Product entity = new Product();
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		
		entity = repository.save(entity);
		
		return new ProductDTO(entity);
		
	}
}