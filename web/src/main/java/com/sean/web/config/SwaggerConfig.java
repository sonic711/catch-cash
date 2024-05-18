package com.sean.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class SwaggerConfig {

	@Value("${server.port}")
	private int serverPort;

	@Value("${server.servlet.context-path}")
	private String path;

	@Bean
	OpenAPI customOpenAPI() {

		String baseUrl = "http://127.0.0.1:" + serverPort + path;

		OpenAPI openAPI = new OpenAPI();
		io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info();

		String version = LocalDate.now().toString();
		String description = "後台管理系統 API Specifications";
		info.title("SWAGGER-DEMO-API").version(version).description(description);
		openAPI.setInfo(info);

		List<Server> servers = new ArrayList<>();
		servers.add(new Server().url(baseUrl));
		openAPI.setServers(servers);
		return openAPI;
	}
}
