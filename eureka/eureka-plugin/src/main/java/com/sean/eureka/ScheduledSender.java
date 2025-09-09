package com.sean.eureka;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sean.eureka.vo.ProviderInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 定期發送主機指標資訊到遠端伺服器（暫定為）Web Server
 */

@Slf4j
@AllArgsConstructor
@Component
public class ScheduledSender {

	private final MetricsService metricsService;
	private final DiscoveryService discoveryService;

	/**
	 * 每 3 秒發送一次主機指標資訊到遠端伺服器
	 */
	@Scheduled(fixedRate = 3000)
	public void sendMetricsToRemote() {
		Map<String, String> metricsData = metricsService.getMetricsInfo();
		try {
			ProviderInfo providerInfo = discoveryService.getProviderInfo(Constant.WEB);
			if (providerInfo != null) {
				RestTemplate restTemplate = new RestTemplate();
				String url = "http://" + providerInfo.getHostName() + ":" + providerInfo.getServerPort() + providerInfo.getContentPath() + "/health";
				ResponseEntity<String> response = restTemplate.postForEntity(url, metricsData, String.class);
				log.debug("Metrics sent successfully: {}", response.getStatusCode());
			}
		} catch (Exception e) {
			log.error("Failed to send metrics: {}", e.getLocalizedMessage());
		}
	}
}
