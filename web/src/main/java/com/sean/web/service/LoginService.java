package com.sean.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sean.web.vo.BasicOut;
import com.sean.web.vo.MemberDetailVO;
import com.sean.web.vo.TokenInfo;
import com.sean.web.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService {

	@Autowired
	private SSOAuthService ssoAuthService;

	public BasicOut<TokenInfo> processLogin(MemberDetailVO input) {
		BasicOut<TokenInfo> result = new BasicOut<>();
		// 假設該user已資料存在DB
		UserInfo userInfo = new UserInfo("Sean", "SeanLiu", "password", "123.123.123.1", "seanliu@gmail.com");

		String account = input.getName();
		String password = input.getPassword();
		if (account.equals(userInfo.getUserName()) && password.equals(userInfo.getUserPassword())) {
			TokenInfo tokenInfo = ssoAuthService.generateLoginToken(userInfo);
			result.setData(tokenInfo);
			result.addMessage("登入成功！");
		} else {
			result.setStatus("f");
			result.addMessage("登入失敗");
		}
		return result;
	}

}
