package com.victoroliveira.dscommerce.controllers.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victoroliveira.dscommerce.dto.OrderDTO;
import com.victoroliveira.dscommerce.entities.Order;
import com.victoroliveira.dscommerce.entities.OrderItem;
import com.victoroliveira.dscommerce.entities.OrderStatus;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.entities.User;
import com.victoroliveira.dscommerce.tests.ProductFactory;
import com.victoroliveira.dscommerce.tests.TokenUtil;
import com.victoroliveira.dscommerce.tests.UserFactory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private String adminToken;
	private String clientToken;	
	private String adminOnlyToken;
	private String adminOnlyUsername;
	private String adminOnlyPassword;
	private String invalidToken;
	private String clientUsername;
	private String clientPassword;
	private String adminUsername;
	private String adminPassword;
	
	private Order order;
	private OrderDTO orderDTO;
	private User user;	
	
	private Long existingId;
	private Long nonExistingId;	
	
	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		nonExistingId = 2000L;	
		
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
		adminOnlyUsername = "ana@gmail.com";
		adminOnlyPassword = "123456";
		
		invalidToken = adminToken + "xpto";		
		adminToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		clientToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		adminOnlyToken = tokenUtil.obtainAccessToken(mockMvc, adminOnlyUsername, adminOnlyPassword);
		
		user = UserFactory.createClientUser();
		order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, user, null);
			
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		orderDTO = new OrderDTO(order);
	}	
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenExistingIdAndAdminLogged() throws Exception {
					
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingId)				
				.header("Authorization", "Bearer " + adminToken)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
		
	}
	
	@Test
	public void findByIdShouldReturnOrderDTOWhenExistingIdAndClientLogged() throws Exception {
					
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingId)				
				.header("Authorization", "Bearer " + clientToken)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.moment").value("2022-07-25T13:00:00Z"));
		result.andExpect(jsonPath("$.status").value("PAID"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.payment").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
		
	}
	
	@Test
	public void findByIdShouldReturnForbiddenWhenExistingIdAndClientLoggedAndOrderDoesNotBelongUser() throws Exception {
					
		Long otherOrderId = 2L;
		
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", otherOrderId)				
				.header("Authorization", "Bearer " + clientToken)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenNonExistingIdAndAdminLogged() throws Exception {
					
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", nonExistingId)				
				.header("Authorization", "Bearer " + adminToken)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenNonExistingIdAndClientLogged() throws Exception {
					
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", nonExistingId)				
				.header("Authorization", "Bearer " + clientToken)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnUnauthorizedWhenExistingIdAndInvalidToken() throws Exception {
					
		ResultActions result = mockMvc
				.perform(get("/orders/{id}", existingId)				
				.header("Authorization", "Bearer " + invalidToken)				
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void insertShouldReturnOrderDTOCreatedWhenClientLogged() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + clientToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").value(4L));
		result.andExpect(jsonPath("$.moment").exists());
		result.andExpect(jsonPath("$.status").value("WAITING_PAYMENT"));
		result.andExpect(jsonPath("$.client").exists());
		result.andExpect(jsonPath("$.items").exists());
		result.andExpect(jsonPath("$.total").exists());
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenClientLoggedAndOrderHasNoItem() throws Exception {
		
		orderDTO.getItems().clear();

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + clientToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andDo(MockMvcResultHandlers.print());
		
		result.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenAdminLogged() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + adminOnlyToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isForbidden());
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

		String jsonBody = objectMapper.writeValueAsString(orderDTO);
		
		ResultActions result = 
				mockMvc.perform(post("/orders")
					.header("Authorization", "Bearer " + invalidToken)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isUnauthorized());
	}
	
}
