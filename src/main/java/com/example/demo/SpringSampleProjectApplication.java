package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages={
	"com.example.demo.repository.customer",
	"com.example.demo.repository.query"	
})
public class SpringSampleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSampleProjectApplication.class, args);
	}

}
