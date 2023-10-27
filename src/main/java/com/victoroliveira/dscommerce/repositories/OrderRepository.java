package com.victoroliveira.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.victoroliveira.dscommerce.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
