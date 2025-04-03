package com.sean.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Component("EurekaClientConfig")
public class Config {
	@Autowired
	private EurekaClient eurekaClient;

	/**
	 * 簡易實現，啟動時 將 InstanceStatus 設為 UP
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		eurekaClient.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
	}

	/**
	 * 簡易實現，關閉時 將 InstanceStatus 設為 DOWN
	 */
	@EventListener(ContextClosedEvent.class)
	public void shutDown() {
		eurekaClient.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
	}
}
