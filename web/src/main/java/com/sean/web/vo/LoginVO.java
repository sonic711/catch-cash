package com.sean.web.vo;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginVO implements Serializable {
	@Serial
	private static final long serialVersionUID = -1080330868240864382L;
	private String userAccount;
	private String userP1;
	private String ip;

}
