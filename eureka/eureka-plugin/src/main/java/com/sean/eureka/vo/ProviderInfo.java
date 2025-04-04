package com.sean.eureka.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProviderInfo {

	private String applicationName;

	private String hostName;

	private Integer serverPort;

	private String grpcPort;

	private String contentPath;

}
