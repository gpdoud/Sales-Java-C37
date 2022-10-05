package com.bootcamp.capstone.tutorial.sales.order;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.tutorial.sales.customer.Customer;
import com.bootcamp.capstone.tutorial.sales.customer.CustomerRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrdersController {
	
	private final String NEW = "NEW";
	private final String REVIEW = "REVIEW";
	private final String APPROVED = "APPROVED";
	private final String REJECTED = "REJECTED";

	@Autowired
	private OrderRepository orderRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Order>> getOrders() {
		Iterable<Order> orders = orderRepo.findAll();
		return new ResponseEntity<Iterable<Order>>(orders, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Order> getOrderByPk(@PathVariable int id) {
		Optional<Order> order = orderRepo.findById(id);
		if(order.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Order>(order.get(), HttpStatus.OK);
	}
	
	@GetMapping("reviews/{customerId}")
	public ResponseEntity<Iterable<Order>> getOrdersInReview(@PathVariable int customerId) {
		Iterable<Order> orders = orderRepo.findByStatusAndCustomerIdNot(REVIEW, customerId);
		return new ResponseEntity<Iterable<Order>>(orders, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Order> postOrder(@RequestBody Order order) {
		if(order.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		order.setStatus(NEW);
		Order newOrder = orderRepo.save(order);
		return new ResponseEntity<Order>(newOrder, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putOrder(@PathVariable int id, @RequestBody Order order) {
		if(order.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var ord = orderRepo.findById(order.getId());
		if(ord.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		orderRepo.save(order);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity reviewOrder(@PathVariable int id, @RequestBody Order order) {
		// order.Status = (order.Total <= 200) ? "APPROVED" : "REVIEW";
		String newStatus = order.getTotal() <= 200 ? APPROVED : REVIEW; 
		order.setStatus(newStatus);
		return putOrder(id, order);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity approveOrder(@PathVariable int id, @RequestBody Order order) {
		order.setStatus(APPROVED);
		return putOrder(id, order);
	}

	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity rejectOrder(@PathVariable int id, @RequestBody Order order) {
		order.setStatus(REJECTED);
		return putOrder(id, order);
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteOrder(@PathVariable int id) {
		var ord = orderRepo.findById(id);
		if(ord.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		orderRepo.delete(ord.get());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	
	
	
	
	
	
	
	
}
