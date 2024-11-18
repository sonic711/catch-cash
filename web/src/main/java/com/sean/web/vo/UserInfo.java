package com.sean.web.vo;

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
public class UserInfo {

	private String userId; // 登入使用者帳號
	private String userName; // 使用者姓名
	private String userPassword; // 使用者密碼
	private String userIp; // 使用者 IP
	private String email;// 信箱

}
