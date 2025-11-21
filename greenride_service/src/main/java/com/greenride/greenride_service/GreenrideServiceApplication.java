package com.greenride.greenride_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // <--- Import this
import org.springframework.data.jpa.repository.config.EnableJpaRepositories; // <--- Import this
import org.springframework.boot.autoconfigure.domain.EntityScan; // <--- Import this

@SpringBootApplication
// 1. Tell Spring where to find your Controllers and Services
@ComponentScan(basePackages = "com.greenride")
// 2. Tell Spring where to find your Database Repositories
@EnableJpaRepositories(basePackages = "com.greenride.repository")
// 3. Tell Spring where to find your Database Entities (User, Ride, etc.)
@EntityScan(basePackages = "com.greenride.model")
public class GreenrideServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenrideServiceApplication.class, args);
	}

}