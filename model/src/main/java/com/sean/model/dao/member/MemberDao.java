package com.sean.model.dao.member;

import com.sean.model.entities.MemberEntity;

import java.util.List;

public interface MemberDao {

    MemberEntity getMember(Integer memberId);

    List<MemberEntity> getMembers();

    void saveMember(MemberEntity member);

    void updateMember(MemberEntity member);

    void deleteMember(Integer memberId);
}
