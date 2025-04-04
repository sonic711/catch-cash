package com.sean.eureka;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.netflix.appinfo.EurekaInstanceConfig;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class MetricsService {

	private final EurekaInstanceConfig eurekaInstanceConfig;
	private final MeterRegistry meterRegistry;

	public Map<String, String> getMetricsInfo() {
		return Map.of(//簡易整理成Map
				"ip", eurekaInstanceConfig.getIpAddress(),//
				"app_name", eurekaInstanceConfig.getAppname(),//
				"instance_id", eurekaInstanceConfig.getInstanceId(),//
				"port", String.valueOf(eurekaInstanceConfig.getNonSecurePort()),//
				"cpu_usage", String.valueOf(getCpuUsage()),//
				"cpu_total", String.valueOf(getCpuTotal()),//
				"jvm_memory_used", String.valueOf(getJvmMemoryUsed())//
		);
	}

	private double getCpuUsage() {
		return meterRegistry.get("system.cpu.usage").gauge().value();
	}

	private double getCpuTotal() {
		return meterRegistry.get("system.cpu.count").gauge().value();
	}

	private double getJvmMemoryUsed() {
		return meterRegistry.get("jvm.memory.used").gauge().value();
	}
}

