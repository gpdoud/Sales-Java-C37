package com.bootcamp.capstone.tutorial.sales.orderline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bootcamp.capstone.tutorial.sales.item.ItemRepository;
import com.bootcamp.capstone.tutorial.sales.order.OrderRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/orderlines")
public class OrderlinesController {

	@Autowired
	private OrderlineRepository ordlRepo;
	@Autowired
	private OrderRepository ordRepo;
	@Autowired
	private ItemRepository itemRepo;
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity recalcOrderTotal(int orderId) {
		var ordOpt = ordRepo.findById(orderId);
		if(ordOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var order = ordOpt.get();
		var orderTotal = 0;
		Iterable<Orderline> orderlines = ordlRepo.findByOrderId(order.getId());
		for(var orderline : orderlines) {
			//var item = itemRepo.findById(orderline.getItem().getId());
			//orderline.setItem(item.get());
			orderTotal += orderline.getItem().getPrice() * orderline.getQuantity();
		}
		order.setTotal(orderTotal);
		ordRepo.save(order);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Iterable<Orderline>> getRequestlines() {
		var requestlines = ordlRepo.findAll();
		return new ResponseEntity<Iterable<Orderline>>(requestlines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Orderline> getRequestline(@PathVariable int id) {
		var prod = ordlRepo.findById(id);
		if(prod.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Orderline>(prod.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Orderline> postRequestline(@RequestBody Orderline orderline) throws Exception {
		if(orderline == null || orderline.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var ordl = ordlRepo.save(orderline);
		ordl = ordlRepo.findById(ordl.getId()).get();
		var respEntity = this.recalcOrderTotal(ordl.getOrder().getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		return new ResponseEntity<Orderline>(ordl, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Orderline orderline) throws Exception {
		if(orderline == null || orderline.getId() == 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var reqlOpt = ordlRepo.findById(orderline.getId());
		if(reqlOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ordlRepo.save(orderline);
		var respEntity = this.recalcOrderTotal(orderline.getOrder().getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteOrderline(@PathVariable int id) throws Exception {
		var ordlOpt = ordlRepo.findById(id);
		if(ordlOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var orderline = ordlOpt.get();
		ordlRepo.delete(orderline);
		var respEntity = this.recalcOrderTotal(orderline.getId());
		if(respEntity.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate request total failed!");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
}
