package com.sean.web.aop;

import java.util.List;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ControllerInterceptor {

	private static final ThreadLocal<Long> startTime = new ThreadLocal<>();
	private static final ThreadLocal<String> reqKey = new ThreadLocal<>();
	private static final List<String> skipLoggingActions = List.of("sys");

	/**
	 * 定義攔截規則：攔截所有類別中，有 @RestController 註解的方法
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void apiPointcut() {}

	/**
	 * 請求方法前記錄內容
	 *
	 * @param joinPoint
	 * @return
	 */
	@Before("apiPointcut()")
	public void doBefore(JoinPoint joinPoint) {
		// Request 開始時間
		startTime.set(System.currentTimeMillis());
		HttpServletRequest request = getRequest();
		if (request != null) {
			// for Around Key
			String uri = request.getRequestURI() + "|" + UUID.randomUUID();
			// OPTIONS 請求不需記錄
			log.info(request.getMethod());
			if ("OPTIONS".equals(request.getMethod()) || skipLoggingActions.stream().anyMatch(uri::contains)) {
				// 不須記錄的動作

			} else {
				log.info("AOP REQUEST url[{}], method[{}] {}", uri, request.getMethod(), request.getRequestURI());
				reqKey.set(uri);
			}
		}
	}

	/**
	 * 在方法執行後記錄返回內容
	 *
	 * @param obj
	 */
	@AfterReturning(returning = "obj", pointcut = "apiPointcut()")
	public void doAfterReturing(Object obj) {
		if (reqKey.get() != null) {
			long l = System.currentTimeMillis();
			long l1 = l - startTime.get();
			log.info("AOP AFTER url[{}] costTime[{}ms]", reqKey.get(), l1);
			startTime.remove();
		}
	}

	private HttpServletRequest getRequest() {
		HttpServletRequest request = null;
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		if (sra != null) {
			request = sra.getRequest();
		}
		return request;
	}

}
