package com.sean.web.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenInfo {
	private String accessToken;
	private Integer expiresIn;
	private String refreshToken;
	private Integer refreshExpiresIn;
	private String tokenType = "bearer";
}
