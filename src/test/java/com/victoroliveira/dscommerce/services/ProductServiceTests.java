package com.victoroliveira.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.victoroliveira.dscommerce.dto.ProductDTO;
import com.victoroliveira.dscommerce.dto.ProductMinDTO;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.repositories.ProductRepository;
import com.victoroliveira.dscommerce.services.exceptions.ResourceNotFoundException;
import com.victoroliveira.dscommerce.tests.ProductFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	ProductService service;
	
	@Mock
	ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private String productName;
	private Product product;	
	private ProductDTO productDTO;
	private PageImpl<Product> page;
	
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L;
		nonExistingId = 1000L;
		
		productName = "Red Dead Redemption 2";
		
		product = ProductFactory.createProduct(productName);
		productDTO = new ProductDTO(product);
		page = new PageImpl<>(List.of(product));
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.searchByName(any(), (Pageable) any())).thenReturn(page);
		
		Mockito.when(repository.save(any())).thenReturn(product);
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenExistingId() {
		
		ProductDTO dto = service.findById(existingId);
		
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(dto.getId(), existingId);
		Assertions.assertEquals(dto.getName(), product.getName());
		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void findAllShouldReturnPagedProductMinDTO() {
		
		Pageable pageable = PageRequest.of(0, 12);		
		
		Page<ProductMinDTO> result = service.findAll(productName, pageable);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getSize(), 1);
		Assertions.assertEquals(result.iterator().next().getName(), productName);
	}
	
	@Test
	public void insertShouldReturnProductDTO() {
		
		ProductDTO result = service.insert(productDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), product.getId());
		Assertions.assertEquals(result.getName(), product.getName());
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenExistingId() {
		
		ProductDTO dto = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(dto);

		Mockito.verify(repository, Mockito.times(1)).save(product);
	}
	
	@Test
	public void updatedShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});		
		
		Mockito.verify(repository, Mockito.times(0)).save(product);
		
	}

}
