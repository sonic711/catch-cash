package com.sean.model.dao.member.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.sean.model.dao.member.MemberDao;
import com.sean.model.entities.MemberEntity;
import com.sean.model.repo.MemberRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Repository
@Slf4j
public class MemberJPA implements MemberDao {

	private final MemberRepo memberRepo;

	@Override
	public MemberEntity getMember(Integer memberId) {
		return memberRepo.findById(memberId).orElse(null);
	}

	@Override
	public List<MemberEntity> getMembers() {
		log.info("JPA getMembers()");
		return memberRepo.findAll();
	}

	@Override
	public void saveMember(MemberEntity member) {
		memberRepo.save(member);
	}

	@Override
	public void updateMember(MemberEntity member) {
		memberRepo.findById(member.getId()).ifPresent(dbMember -> {
			BeanUtils.copyProperties(member, dbMember, "createUser", "createTime");
			dbMember.setUpdateUser("ADMIN123");
			dbMember.setUpdateTime(LocalDateTime.now());
			memberRepo.save(dbMember);
		});
	}

	@Override
	public void deleteMember(Integer memberId) {
		memberRepo.deleteById(memberId);
	}

	@Override
	public void uploadImage(Integer memberId, MultipartFile file) {
		memberRepo.findById(memberId).ifPresent(dbMember -> {
			try {
				dbMember.setProfileImage(file.getBytes());
				dbMember.setUpdateTime(LocalDateTime.now());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			memberRepo.save(dbMember);
		});
	}

	@Override
	public byte[] downloadImage(Integer memberId) {
		// download image from database and return as MultipartFile
		byte[] file = new byte[0];
		Optional<MemberEntity> dbMember = memberRepo.findById(memberId);
		if (dbMember.isPresent()) {
			file = dbMember.get().getProfileImage();
		}
		return file;
	}
}
