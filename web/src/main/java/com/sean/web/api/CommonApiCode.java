package com.sean.web.api;

import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonApiCode implements ApiCode {

	/**
	 * 20000 成功
	 */
	SUCCESS(20000, "success"),
	/**
	 * 50000 未知異常
	 */
	SYSTEM_EXCEPTION(50000, "未知異常"),
	/**
	 * 50001 登入憑證無效
	 */
	JWT_TOKEN_VALIDATE_ERROR(50001, "憑證無效"),
	/**
	 * 50002
	 */
	NO_PERMISSION(50002, "該帳號無權限"),
	/**
	 * 50003 登入憑證過期
	 */
	ACCESS_TOKEN_EXPIRED(50003, "憑證過期"),
	/**
	 * 50004 使用者不存在
	 */
	INVALID_USER(50004, "使用者不存在"),
	/**
	 * 50005 登入資訊錯誤
	 */
	USERNAME_PASSWORD_WRONG(50005, "登入資訊錯誤");

	private final int code;

	private final String message;

	public static CommonApiCode findByCode(int code) {
		return Stream.of(CommonApiCode.values()).filter(d -> d.code == code).findFirst().orElse(null);
	}
}
