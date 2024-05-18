package com.sean.web.service;

import com.sean.model.dao.member.MemberDao;
import com.sean.model.entities.MemberEntity;
import com.sean.web.vo.MemberDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(@Qualifier("memberSQL") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberDetailVO getMember(Integer memberId) {
        MemberDetailVO memberDetailVO = null;
        if (null != memberId) {
            MemberEntity member = memberDao.getMember(memberId);
            memberDetailVO = new MemberDetailVO();
            BeanUtils.copyProperties(member, memberDetailVO);
        } else {
            // todo 可自行處理
        }
        return memberDetailVO;
    }

    public List<MemberEntity> getMembers() {
        return memberDao.getMembers();
    }

    public void saveMember(MemberEntity member) {
        memberDao.saveMember(member);
    }

    public void updateMember(MemberEntity member) {
        memberDao.updateMember(member);
    }

    public void deleteMember(Integer memberId) {
        memberDao.deleteMember(memberId);
    }
}
