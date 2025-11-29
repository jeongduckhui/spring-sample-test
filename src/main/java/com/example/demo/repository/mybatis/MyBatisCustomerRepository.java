package com.example.demo.repository.mybatis;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.Customer;
import com.example.demo.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MyBatisCustomerRepository implements CustomerRepository{

	private final CustomerMapper customerMapper;
	
	@Override
	public Optional<Customer> findById(Long id) {
        return customerMapper.findById(id);
    }
}
