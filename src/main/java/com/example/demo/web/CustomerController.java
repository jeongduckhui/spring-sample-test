package com.example.demo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Customer;
import com.example.demo.service.customer.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	
	@GetMapping("/{customerId}")
	public Customer findCustomerById(@PathVariable("customerId") long customerId) {
		Customer customer = customerService.findById(customerId).get();
		return customer;
	}
}
