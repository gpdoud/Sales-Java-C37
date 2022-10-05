package com.bootcamp.capstone.tutorial.sales.order;

import java.util.List;

import javax.persistence.*;

import com.bootcamp.capstone.tutorial.sales.customer.Customer;
import com.bootcamp.capstone.tutorial.sales.orderline.Orderline;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="Orders")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(length=50, nullable=false)
	private String description = "New Order";
	@Column(columnDefinition="decimal(9,2) not null default 0")
	private double total;
	@Column(length=30, nullable=false)
	private String status;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="customerId", columnDefinition="int")
	private Customer customer;
	
	@JsonManagedReference
	@OneToMany(mappedBy="order")
	private List<Orderline> orderlines;
	
	public List<Orderline> getOrderlines() {
		return orderlines;
	}

	public void setOrderlines(List<Orderline> orderlines) {
		this.orderlines = orderlines;
	}

	public Order() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
}
