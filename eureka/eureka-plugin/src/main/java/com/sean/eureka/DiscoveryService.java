package com.sean.eureka;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.sean.eureka.vo.ProviderInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class DiscoveryService {

	private final static String GRPC_PORT = "gRPCPort";

	private final EurekaClient eurekaClient;

	/**
	 * 取得指定的多個服務
	 *
	 * @param serviceName 服務名稱
	 * @return List<ProviderInfo>
	 */
	@SuppressWarnings("unchecked")
	public List<ProviderInfo> getProviderInfoList(String serviceName) {
		try {
			List<InstanceInfo> instances = this.eurekaClient.getInstancesByVipAddress(serviceName, false);
			if (CollectionUtils.isEmpty(instances)) {
				log.warn("DispatcherService getProviderInfoList not found eureka client instances.");
				return Collections.emptyList();
			} else {
				return instances.stream().map(this::buildProviderInfo).toList();
			}
		} catch (Exception e) {
			log.error("DispatcherService get provider info list by application name : {} occurs exception : {}.", serviceName, e.getLocalizedMessage());
			return Collections.emptyList();
		}
	}

	/**
	 * 取得一個服務
	 *
	 * @param serviceName 服務名稱
	 * @return ProviderInfo
	 */
	public ProviderInfo getProviderInfo(String serviceName) {
		getProviderInfoList(serviceName).stream().findFirst().ifPresent(providerInfo -> {
			log.info("DispatcherService get provider info by application name : {} success, provider info : {}.", serviceName, providerInfo);
		});
		return getProviderInfoList(serviceName).stream().findFirst().orElse(null);
	}

	private ProviderInfo buildProviderInfo(InstanceInfo instance) {
		return new ProviderInfo(instance.getAppName(), instance.getIPAddr(), instance.getPort(), instance.getMetadata().get(GRPC_PORT), getContentPath(instance.getHealthCheckUrl()));
	}

	private String getContentPath(String healthCheckUrl) {
		String path = generateUriComponents(healthCheckUrl).getPath();
		assert path != null;
		int start = path.indexOf("actuator");
		if (start == -1) {
			return null;
		}
		String contentPath = path.substring(0, start);
		if (StringUtils.endsWith(contentPath, "/")) {
			return StringUtils.equals("/", contentPath) ? null : contentPath.substring(0, contentPath.length() - 1);
		}
		return contentPath;
	}

	private UriComponents generateUriComponents(String url) {
		return UriComponentsBuilder.fromUriString(url).build();
	}
}


