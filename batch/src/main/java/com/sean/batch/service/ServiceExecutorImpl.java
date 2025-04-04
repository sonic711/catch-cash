package com.sean.batch.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sean.batch.vo.InputVO;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

//@Component
public class ServiceExecutorImpl implements ServiceExecutor {

	private final Map<String, BtService> serviceMap;

	// 注入所有的 BtService 實現
	public ServiceExecutorImpl(Map<String, BtService> services) {
		this.serviceMap = services;
	}

	public void executeServices(InputVO input, List<String> serviceSequence) {
		for (String serviceId : serviceSequence) {
			BtService service = serviceMap.get(serviceId);
			if (service != null) {
				service.execute(input);
			} else {
				System.out.println("Service " + serviceId + " not found.");
			}
		}
	}
}
