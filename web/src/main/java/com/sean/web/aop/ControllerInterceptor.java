package com.sean.web.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class ControllerInterceptor {
    //    @Value("${jwt.secret}")
//    private String jwtKey;
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    private static ThreadLocal<String> reqKey = new ThreadLocal<>();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static List<String> skipLoggingActions = Arrays.asList("sys");
    private static ThreadLocal<StopWatch> stopWatch = new ThreadLocal<>();

    /**
     * 定義攔截規則：攔截所有類別中，有 @RestController 註解的方法
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void apiPointcut() {
    }

    /**
     * 請求方法前記錄內容
     *
     * @param joinPoint
     * @return
     */
    @Before("apiPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        boolean vali = true;
        // Request 開始時間
        stopWatch.set(StopWatch.createStarted());
        log.info("CONTROLLER AOP STARTING {}", stopWatch.get());
        HttpServletRequest request = getRequest();
        if (request != null) {

        }
    }


    /**
     * 在方法執行後記錄返回內容
     *
     * @param obj
     */
    @AfterReturning(returning = "obj", pointcut = "apiPointcut()")
    public void doAfterReturing(Object obj) {
        // 停止計時
        StopWatch sw = stopWatch.get();
        log.info("CONTROLLER AOP ENDING {}", sw);
        sw.stop();
        long processSeconds = sw.getTime(TimeUnit.MILLISECONDS);
        log.info("process seconds : {}ms", processSeconds);
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
