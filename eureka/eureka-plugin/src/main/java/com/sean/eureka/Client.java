package com.sean.eureka;

import java.util.List;

import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class Client {

	private final EurekaClient eurekaClient;

	public void getInstances() {
		log.info("Getting instances from eureka server");
		List<Application> registeredApplications = eurekaClient.getApplications().getRegisteredApplications();
		List<InstanceInfo> infos = this.eurekaClient.getInstancesByVipAddress("batch", false);

	}
}
