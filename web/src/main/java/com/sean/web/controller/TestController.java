package com.sean.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sean.web.service.TestService;

@RestController
@RequestMapping(value = "/api")
public class TestController {
	private final TestService service;

	public TestController(TestService service) {
		this.service = service;
	}

	@GetMapping("/test")
	public String test() {
		// fix
		return service.test();
	}
}
