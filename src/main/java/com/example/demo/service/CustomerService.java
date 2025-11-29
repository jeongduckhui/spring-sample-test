package com.example.demo.service;

import java.util.Optional;

import com.example.demo.domain.Customer;

public interface CustomerService {

	Optional<Customer> findById(Long id);
}
