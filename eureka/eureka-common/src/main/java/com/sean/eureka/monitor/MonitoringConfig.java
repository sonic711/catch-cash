package com.sean.eureka.monitor;

import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MonitoringConfig implements ConfigurationListener {

	@Override
	public void onChange(Reconfigurable reconfigurable) {
		log.info("Monitoring Configuration changed ...");
	}
}
