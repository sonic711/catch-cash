package com.sean.eureka;

import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("EurekaClientConfig")
@AllArgsConstructor
public class Config {

	private final EurekaClient eurekaClient;
	private final HealthEndpoint healthEndpoint;

	/**
	 * 簡易實現，啟動時 將 InstanceStatus 設為 UP
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		checkAndUpdateInstanceStatus();
	}

	/**
	 * 每 30 秒重新檢查健康狀態
	 */
	@Scheduled(fixedRate = 30000) // 每 30 秒執行一次
	public void checkAndUpdateInstanceStatus() {
		log.info("Checking instance status");
		// 從整體Status判斷健康狀態
		if (Status.UP.equals(healthEndpoint.health().getStatus())) {
			log.info("Instance is healthy");
			eurekaClient.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
		} else {
			log.info("Instance is unhealthy");
			shutDown();
		}
	}

	/**
	 * 簡易實現，關閉時 將 InstanceStatus 設為 DOWN
	 */
	@EventListener(ContextClosedEvent.class)
	public void shutDown() {
		log.info("Shutting down instance");
		eurekaClient.getApplicationInfoManager().setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
	}
}
