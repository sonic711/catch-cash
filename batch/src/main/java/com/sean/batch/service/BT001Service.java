package com.sean.batch.service;

import org.springframework.stereotype.Service;

import com.sean.batch.vo.InputVO;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service("BT001")
public class BT001Service implements BtService {

	@PostConstruct
	public void init() {
		System.out.println("Initializing BT001Service");
	}

	@PreDestroy
	public void destroy() {
		System.out.println("Destroying BT001Service");
	}

	@Override
	public void execute(InputVO input) {
		// 實作 BT001 的邏輯
		System.out.println("Executing BT001");
	}

	@Override
	public String getServiceId() {
		return "BT001";
	}
}
