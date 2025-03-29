package com.sean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class BatchServiceApplication extends SpringBootServletInitializer {
	@PostConstruct
	public void init() {
		System.out.println("Initializing BatchServiceApplication");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Destroying BatchServiceApplication");
	}

	public static void main(String[] args) {
		SpringApplication.run(BatchServiceApplication.class, args);
	}

}
