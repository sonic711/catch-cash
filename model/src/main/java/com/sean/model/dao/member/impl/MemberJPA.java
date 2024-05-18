package com.sean.model.dao.member.impl;

import com.sean.model.dao.member.MemberDao;
import com.sean.model.entities.MemberEntity;
import com.sean.model.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            memberRepo.save(dbMember);
        });
    }

    @Override
    public void deleteMember(Integer memberId) {
        memberRepo.deleteById(memberId);
    }
}
