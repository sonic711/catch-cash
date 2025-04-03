package com.sean.batch.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 版本
 */
public class VersionCommand {

	/**
	 * 完整指令
	 */
	private final static String version = "--version";
	/**
	 * 短寫指令
	 */
	private final static String v = "-v";
	private final static List<String> list = Arrays.asList(version, v);

	/**
	 * 是否為命令列執行
	 */
	public static boolean executable(final String key) {
		return list.contains(key);
	}

	/**
	 * 執行
	 */
	public static String execute(final String key) {
		if (executable(key)) {
			return new GitPropertiesUtil().toString();
		} else {
			return "AbstractVersion Command Line parameter unrecognized";
		}
	}
}
