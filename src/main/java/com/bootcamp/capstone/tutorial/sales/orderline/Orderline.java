package com.bootcamp.capstone.tutorial.sales.orderline;

import javax.persistence.*;

import com.bootcamp.capstone.tutorial.sales.item.Item;
import com.bootcamp.capstone.tutorial.sales.order.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name="orderlines")
public class Orderline {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int quantity;
	
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="orderId")
	private Order order;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="itemId")
	private Item item;
	
	public Orderline() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	
}
