package com.sean.web.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sean.model.entities.MemberEntity;
import com.sean.model.repo.MemberRepo;
import com.sean.web.vo.BasicOut;
import com.sean.web.vo.MemberDetailVO;
import com.sean.web.vo.TokenInfo;
import com.sean.web.vo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService {

	@Autowired
	private SSOAuthService ssoAuthService;
	@Autowired
	private MemberRepo memberRepo;

	public BasicOut<TokenInfo> processLogin(MemberDetailVO input) {
		BasicOut<TokenInfo> result = new BasicOut<>();
		result.setStatus("f");
		result.addMessage("登入失敗");

		Optional<MemberEntity> userFromDB = memberRepo.findByNameAndPassword(input.getName(), input.getPassword());
		if (userFromDB.isPresent()) {
			MemberEntity userDB = userFromDB.get();

			UserInfo userInfo = new UserInfo();
			userInfo.setUserName(userDB.getName());
			userInfo.setUserPassword(userDB.getPassword());
			// TODO 其他屬性

			String account = input.getName();
			String password = input.getPassword();
			if (account.equals(userInfo.getUserName()) && password.equals(userInfo.getUserPassword())) {
				TokenInfo tokenInfo = ssoAuthService.generateLoginToken(userInfo);
				result.setData(tokenInfo);
				result.addMessage("登入成功！");
			}
		}
		return result;
	}

}
