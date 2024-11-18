package com.sean.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sean.web.service.LoginService;
import com.sean.web.vo.BasicOut;
import com.sean.web.vo.MemberDetailVO;
import com.sean.web.vo.TokenInfo;

import lombok.RequiredArgsConstructor;

import static com.sean.web.api.ApiPathConstant.SYS_PATH;

@RestController
@RequestMapping(value = SYS_PATH + "/login")
@RequiredArgsConstructor
public class LoginController {

	@Autowired
	private LoginService loginService;

	@PostMapping
	public BasicOut<TokenInfo> login(@RequestBody MemberDetailVO memberDetailVO) {
		return loginService.processLogin(memberDetailVO);
	}
}
