package com.sean.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sean.batch.vo.InputVO;
import com.sean.model.dao.member.MemberDao;

@Service("BT001")
public class BT001Service implements BtService {

	@Qualifier("memberJPA")
	@Autowired
	private MemberDao memberDao;

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
