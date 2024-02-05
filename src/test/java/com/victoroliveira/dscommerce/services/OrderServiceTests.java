package com.victoroliveira.dscommerce.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.victoroliveira.dscommerce.dto.OrderDTO;
import com.victoroliveira.dscommerce.entities.Order;
import com.victoroliveira.dscommerce.entities.OrderItem;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.entities.User;
import com.victoroliveira.dscommerce.repositories.OrderItemRepository;
import com.victoroliveira.dscommerce.repositories.OrderRepository;
import com.victoroliveira.dscommerce.repositories.ProductRepository;
import com.victoroliveira.dscommerce.services.exceptions.ForbiddenException;
import com.victoroliveira.dscommerce.services.exceptions.ResourceNotFoundException;
import com.victoroliveira.dscommerce.tests.OrderFactory;
import com.victoroliveira.dscommerce.tests.ProductFactory;
import com.victoroliveira.dscommerce.tests.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

	@InjectMocks
	private OrderService service;

	@Mock
	private OrderRepository repository;

	@Mock
	private AuthService authService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Mock
	private UserService userService;

	private Long existingOrderId;
	private Long nonExistingOrderId;
	private Long existingProductId;
	private Long nonExistingProductId;
	private Order order;
	private OrderDTO orderDTO;
	private User admin;
	private User client;

	private Product product;

	@BeforeEach
	void setUp() throws Exception {

		existingOrderId = 1L;
		nonExistingOrderId = 2L;

		existingProductId = 1L;
		nonExistingProductId = 2L;

		admin = UserFactory.createCustomAdminUser(1L, "Bob");
		client = UserFactory.createCustomClientUser(2L, "Maria");

		order = OrderFactory.createOrder(client);

		orderDTO = new OrderDTO(order);

		product = ProductFactory.createProduct();

		Mockito.when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
		Mockito.when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

		Mockito.when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
		Mockito.when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(repository.save(any())).thenReturn(order);
		Mockito.when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));

	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenExistingIdAndAdminLogged() {

		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO result = service.findById(existingOrderId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);

	}

	@Test
	public void findByIdShouldReturnOrderDTOWhenExistingIdAndSelfClientLogged() {

		Mockito.doNothing().when(authService).validateSelfOrAdmin(any());

		OrderDTO result = service.findById(existingOrderId);

		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), existingOrderId);

	}

	@Test
	public void findByIdShouldThrowForbiddenExceptionWhenExistingIdAndOrderClientLogged() {

		Mockito.doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

		Assertions.assertThrows(ForbiddenException.class, () -> {

			@SuppressWarnings("unused")
			OrderDTO result = service.findById(existingOrderId);

		});

	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId() {

		Mockito.doThrow(ResourceNotFoundException.class).when(authService).validateSelfOrAdmin(any());

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {

			@SuppressWarnings("unused")
			OrderDTO result = service.findById(nonExistingOrderId);
		});
	}

	@Test
	public void insertShouldReturnOrderDTOWhenAdminLogged() {

		Mockito.when(userService.authenticated()).thenReturn(admin);

		OrderDTO result = service.insert(orderDTO);

		Assertions.assertNotNull(result);

	}

	@Test
	public void insertShouldReturnOrderDTOWhenClientLogged() {

		Mockito.when(userService.authenticated()).thenReturn(client);

		OrderDTO result = service.insert(orderDTO);

		Assertions.assertNotNull(result);

	}

	@Test
	public void insertShouldThrowUserNotFoundExceptionWhenUserNotLogged() {

		Mockito.doThrow(UsernameNotFoundException.class).when(userService).authenticated();

		order.setClient(new User());
		orderDTO = new OrderDTO(order);

		Assertions.assertThrows(UsernameNotFoundException.class, () -> {

			@SuppressWarnings("unused")
			OrderDTO result = service.insert(orderDTO);
		});

	}

	@Test
	public void insertShouldThrowEntityNotFoundExceptionWhenOrderProductIdDoesNotExist() {

		Mockito.when(userService.authenticated()).thenReturn(client);

		product.setId(nonExistingProductId);
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		orderDTO = new OrderDTO(order);
		
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			
			@SuppressWarnings("unused")
			OrderDTO result = service.insert(orderDTO);
			
		});

	}

}
