package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.domain.Customer;

public interface CustomerRepository {

	public Optional<Customer> findById(Long id);
}
