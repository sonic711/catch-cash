package com.sean.web.service;

import org.springframework.stereotype.Service;

import com.sean.model.entities.MemberEntity;
import com.sean.model.repo.MemberRepo;

@Service
public class TestService {

	private final MemberRepo memberRepo;

	public TestService(MemberRepo memberRepo) {this.memberRepo = memberRepo;}

	public String test() {
		MemberEntity test = memberRepo.findByEmail("test");
		return "Hello World!";
	}
}
