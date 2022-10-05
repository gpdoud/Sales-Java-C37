package com.bootcamp.capstone.tutorial.sales.order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
	Iterable<Order> findByStatusAndCustomerIdNot(String status, int customerId);
}
