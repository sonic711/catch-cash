package com.sean.batch.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sean.batch.service.BtService;
import com.sean.batch.service.ServiceExecutor;
import com.sean.batch.service.ServiceExecutorImpl;

@Configuration
public class BeanConfig {

	private final Map<String, BtService> serviceMap;

	public BeanConfig(Map<String, BtService> serviceMap) {
		this.serviceMap = serviceMap;
	}

	@Bean
	ServiceExecutor serviceExecutor() {
		return new ServiceExecutorImpl(serviceMap);
	}
}
