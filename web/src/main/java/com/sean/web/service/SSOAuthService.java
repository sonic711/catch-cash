package com.sean.web.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sean.web.api.CommonApiCode;
import com.sean.web.utils.TokenUtils;
import com.sean.web.vo.BasicOut;
import com.sean.web.vo.TokenInfo;
import com.sean.web.vo.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SSOAuthService {

	@Value("${jwt.sWord}")
	private String jwtKey;
	@Value("${jwt.expiration}")
	private Integer expiration;
	@Value("${jwt.refreshtoken.expiration}")
	private Integer refreshTokenExpiration;

	/**
	 * 取得登入者資料
	 * <p>
	 * 使用 Interceptor & Controller ，Filter 會先驗證 Token 是否有效
	 *
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UserInfo getUserProfile(String token) {
		UserInfo info = new UserInfo();
		Claims claims = TokenUtils.parseJWT(jwtKey, token);
		info.setUserId(String.valueOf(claims.get("userId")));// 使用者帳號
		info.setUserName(String.valueOf(claims.get("userName")));// 使用者名稱
		info.setUserIp(String.valueOf(claims.get("userIp")));// IP
		info.setEmail(String.valueOf(claims.get("email")));
		return info;
	}

	public TokenInfo generateLoginToken(UserInfo info) {
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setAccessToken(genAccessLoginToken(info));
		tokenInfo.setExpiresIn(expiration);
		tokenInfo.setRefreshToken(getRefreshLoginToken(info));
		tokenInfo.setRefreshExpiresIn(refreshTokenExpiration);
		return tokenInfo;
	}

	private String genAccessLoginToken(UserInfo info) {
		final Claims claims = Jwts.claims();
		try {
			claims.setSubject(info.getUserId());
			// 設定 Token 有效時間
			claims.setExpiration(Date.from(LocalDateTime.now().plusSeconds(expiration).atZone(ZoneId.systemDefault()).toInstant()));
			claims.put("userId", info.getUserId());
			claims.put("userIp", info.getUserIp());
			claims.put("userName", info.getUserName());
			claims.put("email", info.getEmail());

		} catch (Exception e) {
			log.error("發生未知異常:{}", e.getLocalizedMessage());
		}
		return TokenUtils.createJWT(jwtKey, claims);
	}

	/**
	 * 產生 refresh Token
	 *
	 * @param info
	 * @return
	 */
	public String getRefreshLoginToken(UserInfo info) {
		LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(refreshTokenExpiration);
		Date date = Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
		String userId = Optional.ofNullable(info.getUserId()).orElse(StringUtils.EMPTY);
		final Claims claims = Jwts.claims();
		claims.setSubject(userId);
		claims.setExpiration(date);
		return TokenUtils.createJWT(jwtKey, claims);
	}

	/**
	 * 驗證 Token 是否有效
	 *
	 * @param token
	 * @return
	 */
	public BasicOut<Boolean> validateToken(String token) {
		var res = new BasicOut<Boolean>();
		res.setData(Boolean.TRUE);
		try {
			TokenUtils.parseJWT(jwtKey, token);
			res.setData(Boolean.FALSE);
		} catch (ExpiredJwtException e) {
			log.error("jwt token is expired...");
			res.error(CommonApiCode.ACCESS_TOKEN_EXPIRED);
		} catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
			log.error("jwt token validate error...");
			res.error(CommonApiCode.JWT_TOKEN_VALIDATE_ERROR);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			res.error();
		}
		return res;
	}

}
