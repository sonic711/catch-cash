package com.sean.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sean.web.api.ApiPathConstant;
import com.sean.web.api.CommonApiCode;
import com.sean.web.api.ProcessStatusEnum;
import com.sean.web.service.SSOAuthService;
import com.sean.web.vo.BasicOut;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	// 直接跳過驗證的白名單功能
	private List<String> whiteList = List.of("/sys/");

	@Autowired
	private SSOAuthService ssoAuthService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("JWTAuthenticationFilter.doFilterInternal Start!");
		ObjectMapper mapper = new ObjectMapper();
		response.setStatus(HttpStatus.OK.value());
		// response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*"); // 或者指定明確的來源 URL
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Access-Token");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition, Cache-Control, Content-Type");
		log.info("JWTAuthenticationFilter.doFilterInternal 請求方法");
		BasicOut<String> result = new BasicOut<>();
		try {
			if (!StringUtils.equalsIgnoreCase("OPTIONS", request.getMethod())) {
				String jwtToken = request.getHeader("x-access-token");
				if (StringUtils.isEmpty(jwtToken) && request.getRequestURI().contains(ApiPathConstant.SYS_PATH)) {
					filterChain.doFilter(request, response);
					return;
				}
				BasicOut<Boolean> validateToken = ssoAuthService.validateToken(jwtToken);
				if (!validateToken.getData()) {
					// 驗證成功

				} else {
					result.setCode(validateToken.getCode());
					result.setStatus(ProcessStatusEnum.ERROR.getStatus());
					result.setMessage(validateToken.getMessage());
					mapper.writeValue(response.getWriter(), result);
					return;
				}
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error("JWTAuthenticationFilter.doFilterInternal 發生例外:{}", e.getLocalizedMessage());
			result.setCode(CommonApiCode.SYSTEM_EXCEPTION);
			result.setStatus(ProcessStatusEnum.ERROR.getStatus());
			result.setMessage(Arrays.asList(CommonApiCode.SYSTEM_EXCEPTION.getMessage(), e.getLocalizedMessage()));
			mapper.writeValue(response.getWriter(), result);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		boolean skipFilter = true;
		// 只過濾 "/api/"開頭的 request
		if (request.getRequestURI().contains(ApiPathConstant.BASE_PATH + "/")) {
			String resUrl = request.getRequestURI();
			int start = resUrl.indexOf(ApiPathConstant.BASE_PATH) + ApiPathConstant.BASE_PATH.length();
			String path = resUrl.substring(start);
			// 排除非 等於 /sys，skip filter
			if (whiteList.stream().noneMatch(path::contains)) {
				log.debug("path : " + path);
				skipFilter = false;
			}
		}
		return skipFilter;
	}

}
