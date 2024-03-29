package com.victoroliveira.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.victoroliveira.dscommerce.dto.CategoryDTO;
import com.victoroliveira.dscommerce.dto.ProductDTO;
import com.victoroliveira.dscommerce.dto.ProductMinDTO;
import com.victoroliveira.dscommerce.entities.Category;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.repositories.ProductRepository;
import com.victoroliveira.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {			
		
		Product product = repository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("Recurso não encontrado"));			
		return new ProductDTO(product);		
		
	}
	
	@Transactional(readOnly = true)
	public Page<ProductMinDTO> findAll(String name, Pageable pageable) {		
		Page<Product> result = repository.searchByName(name, pageable);			
		return result.map(x -> new ProductMinDTO(x));
		
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {		
		
		Product entity = new Product();
		copyDtoToEntity(dto, entity);		
		entity = repository.save(entity);		
		return new ProductDTO(entity);
		
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {		
		try {
		Product entity = repository.getReferenceById(id);		
		copyDtoToEntity(dto, entity);		
		entity = repository.save(entity);		
		return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new EntityNotFoundException("Recurso não encontrado");
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void deleteById(Long id) {		
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
		repository.deleteById(id);	
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Falha de integridade referencial");
		}
	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		
		entity.getCategories().clear();
		for(CategoryDTO catDTO : dto.getCategories()) {
			Category cat = new Category();
			cat.setId(catDTO.getId());
			entity.getCategories().add(cat);
		}
		
	}
}