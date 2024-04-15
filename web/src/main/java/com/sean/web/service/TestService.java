package com.sean.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sean.model.entities.MemberEntity;
import com.sean.model.repo.MemberRepo;

@Service
public class TestService {

	private final MemberRepo memberRepo;

	public TestService(MemberRepo memberRepo) {this.memberRepo = memberRepo;}

	public String test() {
		List<MemberEntity> test = memberRepo.findAll();
		return test.toString();
	}
}
