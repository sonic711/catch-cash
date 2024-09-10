package com.sean.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sean.web.vo.MemberDetailVO;

import lombok.RequiredArgsConstructor;

import static com.sean.web.api.ApiPathConstant.SYS_PATH;

@RestController
@RequestMapping(value = SYS_PATH + "/login")
@RequiredArgsConstructor
public class LoginController {

	@PostMapping
	public String login(@RequestBody MemberDetailVO memberDetailVO) {
		if (null == memberDetailVO || null == memberDetailVO.getEmail() || null == memberDetailVO.getPassword()) {
			return "login fail";
		}
		if (memberDetailVO.getEmail().equals("admin") && memberDetailVO.getPassword().equals("admin")) {
			return "login success";
		}
		return "login fail";
	}
}
