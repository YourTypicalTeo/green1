package com.greenride.greenride_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.greenride")
@EnableJpaRepositories(basePackages = "com.greenride.repository")
@EntityScan(basePackages = "com.greenride.model")
public class GreenrideServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenrideServiceApplication.class, args);
	}

	// Register RestTemplate Bean for External API calls
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}