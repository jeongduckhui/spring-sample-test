package com.example.demo.service.customer;

import java.util.Optional;

import com.example.demo.domain.Customer;

public interface CustomerService {

	Optional<Customer> findById(Long id);
}
