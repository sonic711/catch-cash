package com.sean.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HealthController {
	@PostMapping
	public String login(@RequestBody String body) {
		log.info(body);
		return "success";
	}
}
