package com.example.demo.service.customer;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Customer;
import com.example.demo.repository.customer.CustomerMapper;

import lombok.RequiredArgsConstructor;

@Service // <--- AOP 적용의 핵심!
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerMapper customerMapper;

	@Override
	public Optional<Customer> findById(Long id) {
		return customerMapper.findById(id);
	} 
    
    
}