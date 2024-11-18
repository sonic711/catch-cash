package com.sean.web.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenUtils {
	/**
	 * create token by login
	 *
	 * @param secret
	 * @param claims
	 * @return
	 */
	public static String createJWT(String secret, Claims claims) {
		return Jwts.builder().setClaims(claims).signWith(Keys.hmacShaKeyFor(createPadded(secret)), SignatureAlgorithm.HS512).compact();
	}

	/**
	 * 轉換為byte[64]
	 *
	 * @param secret
	 * @return
	 */
	private static byte[] createPadded(String secret) {
		return Arrays.copyOf(secret.getBytes(StandardCharsets.UTF_8), 64);
	}

	/**
	 * parse token
	 *
	 * @param secret
	 * @param jwt
	 * @return
	 */
	public static Claims parseJWT(String secret, String jwt) {
		return Jwts.parserBuilder().setSigningKey(createPadded(secret)).build().parseClaimsJws(jwt).getBody();
	}

}
