package com.sean.model.dao.member;

import com.sean.model.entities.MemberEntity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface MemberDao {

    MemberEntity getMember(Integer memberId);

    List<MemberEntity> getMembers();

    void saveMember(MemberEntity member);

    void updateMember(MemberEntity member);

    void deleteMember(Integer memberId);

    void uploadImage(Integer memberId, MultipartFile file);

    byte[] downloadImage(Integer memberId);
}
