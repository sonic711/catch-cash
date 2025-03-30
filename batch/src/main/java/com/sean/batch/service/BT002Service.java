package com.sean.batch.service;

import org.springframework.stereotype.Service;

import com.sean.batch.vo.InputVO;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service("BT002")
public class BT002Service implements BtService {


	@Override
	public void execute(InputVO input) {
		// 實作 BT001 的邏輯
		System.out.println("Executing BT002");
	}

	@Override
	public String getServiceId() {
		return "BT002";
	}
}
