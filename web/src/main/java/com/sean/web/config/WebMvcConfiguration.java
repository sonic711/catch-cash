package com.sean.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] exposedHeaders = new String[] { "Content-Disposition", "Cache-Control", "Content-Type" };
		registry // Cors 註冊
				.addMapping("/**") // for url mapping
				.allowedOrigins("*") // for from side filter
				.allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") // for request method
				.allowedHeaders("*").exposedHeaders(exposedHeaders);
	}

	/**
	 * 防止靜態頁面無法被引入
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		if (!registry.hasMappingForPattern("/**")) {
			registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
		}
	}

}
