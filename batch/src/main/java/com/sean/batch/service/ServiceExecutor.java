package com.sean.batch.service;

import java.util.List;

import com.sean.batch.vo.InputVO;

/**
 * ====================================================================== <br>
 * Licensed Materials - Property of BlueTechnology Corp., Ltd. <br>
 * 藍科數位科技股份有限公司版權所有翻印必究 <br>
 * (C) Copyright BlueTechnology Corp., Ltd. 2019 All Rights Reserved. <br>
 * 日期：2024/12/7<br>
 * 作者：Sean Liu<br>
 * 程式代號: ServiceExecutor<br>
 * 程式說明: <br>
 * ======================================================================
 */
public interface ServiceExecutor {
	void executeServices(InputVO input, List<String> serviceSequence);
}
