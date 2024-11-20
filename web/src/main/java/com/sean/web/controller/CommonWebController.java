package com.sean.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sean.web.service.SSOAuthService;
import com.sean.web.vo.UserInfo;

import jakarta.servlet.http.HttpServletRequest;

public abstract class CommonWebController {

	@Autowired
	private SSOAuthService ssoAuthService;

	/**
	 * 直接利用 request header 中的 token 取得登入者資料
	 */
	public UserInfo getUserInfo() {
		UserInfo info = null;
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		if (sra != null) {
			HttpServletRequest request = sra.getRequest();
			if (request.getHeader("X-Access-Token") != null) {
				String jwtToken = request.getHeader("X-Access-Token");
				info = ssoAuthService.getUserProfile(jwtToken);
			}
		}
		return info;
	}
}
