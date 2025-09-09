package com.sean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// for servlet component scan
@ServletComponentScan(basePackages = "com.sean.web.controller")
// WebServiceApplication抽到外層了，所以下面注解都不需要了
// @EntityScan(basePackages = { "com.sean.web" ,"com.sean.model"})
// @EnableJpaRepositories(basePackages = { "com.sean.web" ,"com.sean.model"})
@EnableScheduling
public class WebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebServiceApplication.class, args);
	}

}
