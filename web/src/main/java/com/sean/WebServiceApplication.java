package com.sean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// WebServiceApplication抽到外層了，所以下面注解都不需要了
// @EntityScan(basePackages = { "com.sean.web" ,"com.sean.model"})
// @EnableJpaRepositories(basePackages = { "com.sean.web" ,"com.sean.model"})
public class WebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebServiceApplication.class, args);
	}

}
