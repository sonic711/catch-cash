package com.sean.web.service;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sean.model.entities.DepartmentEntity;
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
		if (memberRepo.count() == 0) {
			MemberEntity member = new MemberEntity();
			member.setName("ADMIN");
			//			member.setEmail("123");
			member.setPassword("ADMIN");
			member.setCreateUser("initial");
			DepartmentEntity department = new DepartmentEntity();
			department.setDName("台積電");
			department.setCreateUser("initial");
			member.setDepartment(department);
			memberRepo.save(member);
		}
	}

}
