package com.sean.web.service;

import com.sean.model.dao.member.MemberDao;
import com.sean.model.entities.MemberEntity;
import com.sean.web.vo.MemberDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(@Qualifier("memberSQL") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberDetailVO getMember(Integer memberId) {
        MemberDetailVO memberDetailVO = null;
        try {

            if (null != memberId) {
                MemberEntity member = memberDao.getMember(memberId);
                memberDetailVO = new MemberDetailVO();
                BeanUtils.copyProperties(member, memberDetailVO);
            } else {
                // 肯定找不到資料，直接回傳空的VO
                memberDetailVO = new MemberDetailVO();
            }
        } catch (Exception e) {
            log.error("getMember error", e);
        }
        return memberDetailVO;
    }

    public List<MemberEntity> getMembers() {
        return memberDao.getMembers();
    }

    public void saveMember(MemberEntity member) {
        member.setCreateUser("ADMIN");
        memberDao.saveMember(member);
    }

    public void updateMember(MemberEntity member) {
        member.setUpdateUser("ADMIN123");
        memberDao.updateMember(member);
    }

    public void deleteMember(Integer memberId) {
        memberDao.deleteMember(memberId);
    }
    // 上傳圖片
    public void uploadImg(Integer memberId, @RequestParam("file") MultipartFile file){
        memberDao.uploadImage(memberId, file);
    }

    // 下載圖片
    public byte[] downloadImg(Integer memberId){
        return memberDao.downloadImage(memberId);
    }
}
