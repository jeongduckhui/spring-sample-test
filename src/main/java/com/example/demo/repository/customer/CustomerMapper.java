package com.example.demo.repository.customer;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Customer;

@Mapper
public interface CustomerMapper {

	Optional<Customer> findById(@Param("id") Long id);
}
