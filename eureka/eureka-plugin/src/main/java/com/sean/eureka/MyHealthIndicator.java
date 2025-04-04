package com.sean.eureka;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component("myHealthIndicator")// 名稱為 XXXHealthIndicator，XXX會成為健康檢查的名稱
public class MyHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		if (isHealthy()) {
			return Health.up()//
					.withDetail("Message", "Service is up and running")//
					.build();
		}
		return Health.down()//
				.withDetail("Error Message", "Service is down")//
				.build();
	}

	protected boolean isHealthy() {
		// 自定義健康檢查
		return true;
	}

}
