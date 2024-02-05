package com.victoroliveira.dscommerce.tests;

import java.time.Instant;

import com.victoroliveira.dscommerce.entities.Order;
import com.victoroliveira.dscommerce.entities.OrderItem;
import com.victoroliveira.dscommerce.entities.OrderStatus;
import com.victoroliveira.dscommerce.entities.Payment;
import com.victoroliveira.dscommerce.entities.Product;
import com.victoroliveira.dscommerce.entities.User;

public class OrderFactory {
	
	public static Order createOrder(User client) {
		
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING_PAYMENT, client, new Payment());
		
		Product product = ProductFactory.createProduct();
		OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
		order.getItems().add(orderItem);
		
		return order;
		
	}

}
