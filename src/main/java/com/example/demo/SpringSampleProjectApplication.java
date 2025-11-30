package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan(basePackages={
//	"com.example.demo.repository.customer",
//	"com.example.demo.repository.query"	
//})
// Mapper 상위 패키지 지정
@MapperScan(basePackages = "com.example.demo.repository")
public class SpringSampleProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSampleProjectApplication.class, args);
	}

}
