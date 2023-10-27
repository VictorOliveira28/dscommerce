package com.victoroliveira.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victoroliveira.dscommerce.dto.OrderDTO;
import com.victoroliveira.dscommerce.dto.OrderItemDTO;
import com.victoroliveira.dscommerce.entities.Order;
import com.victoroliveira.dscommerce.entities.OrderItem;
import com.victoroliveira.dscommerce.entities.OrderStatus;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.entities.User;
import com.victoroliveira.dscommerce.repositories.OrderItemRepository;
import com.victoroliveira.dscommerce.repositories.OrderRepository;
import com.victoroliveira.dscommerce.repositories.ProductRepository;
import com.victoroliveira.dscommerce.services.exceptions.ResourceNotFoundException;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {			
		Order order = repository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("Recurso não encontrado"));			
		return new OrderDTO(order);		
		
	}

	@Transactional
	public OrderDTO insert(OrderDTO dto) {
		
		Order order = new Order();
		order.setMoment(Instant.now());
		order.setStatus(OrderStatus.WAITING_PAYMENT);
		
		User user = userService.authenticated();
		order.setClient(user);
		
		for(OrderItemDTO itemDto : dto.getItems()) {
			Product product = productRepository.getReferenceById(itemDto.getProductId());
			OrderItem item = new OrderItem(order, product, itemDto.getQuantity(), product.getPrice());
			order.getItems().add(item);
		}
		
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());
		
		return new OrderDTO(order);
	}

}
