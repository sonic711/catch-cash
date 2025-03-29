package com.sean.batch.service;

import com.sean.batch.vo.InputVO;

public interface BtService {
	void execute(InputVO input);

	String getServiceId(); // 用於識別各個Service
}
