package com.sean.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.sean.socket.channel.EchoChannel;

@Configuration
public class SocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpointExporter (){

		ServerEndpointExporter exporter = new ServerEndpointExporter();

		// 手动注册 WebSocket 端点
		exporter.setAnnotatedEndpointClasses(EchoChannel.class);

		return exporter;
	}
}
