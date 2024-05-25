package com.sean.web.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
public class EncryptConfiguration {
	public static final String ENCRYPTOR_BEAN = "encryptorBean";
	private static final StringEncryptor ENCRYPTOR;
	static {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("fsapadm");
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		ENCRYPTOR = encryptor;
	}

	@Bean(name = ENCRYPTOR_BEAN)
	StringEncryptor stringEncryptor() {
		return ENCRYPTOR;
	}

	public static String decrypt(String input) {
		if (null == ENCRYPTOR || !StringUtils.hasText(input)) {
			return input;
		}
		return ENCRYPTOR.decrypt(input);
	}

	public static String encrypt(String input) {
		if (null == ENCRYPTOR || !StringUtils.hasText(input)) {
			return input;
		}
		return ENCRYPTOR.encrypt(input);
	}
}
