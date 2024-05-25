package com.sean.web.service;

import java.io.IOException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sean.model.entities.MemberEntity;
import com.sean.model.repo.MemberRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitalDataService {

	private final MemberRepo memberRepo;

	@EventListener(ApplicationReadyEvent.class)
	public void doForDbInitialData() {
		initMemberData();

	}

	private void initMemberData() {
		if(memberRepo.count() == 0){
			MemberEntity member = new MemberEntity();
			member.setName("ADMIN");
			member.setPassword("ADMIN");
			member.setCreateUser("initial");
			memberRepo.save(member);
		}
	}
}
