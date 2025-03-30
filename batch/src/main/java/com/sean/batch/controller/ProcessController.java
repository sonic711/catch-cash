package com.sean.batch.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sean.batch.service.ServiceExecutor;
import com.sean.batch.vo.InputVO;

@RestController
@RequestMapping("/process")
public class ProcessController {

	private final ServiceExecutor serviceExecutor;

	public ProcessController(ServiceExecutor serviceExecutor) {
		this.serviceExecutor = serviceExecutor;
	}

	@PostMapping("/start")
	public String startProcess(@RequestBody InputVO input, @RequestParam List<String> services) {
		serviceExecutor.executeServices(input, services);
		return "Process completed.";
	}

	@GetMapping("/start")
	public String startProcess() {
		serviceExecutor.executeServices(new InputVO(), new ArrayList<>());
		return "Process completed.";
	}
}
