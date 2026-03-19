package com.limecoding.api.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("requestStartTime", System.currentTimeMillis());

        return true;    // 요청이 계속되도록 설정
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (long) request.getAttribute("requestStartTime");
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        log.info("[REQUEST] {} {} - {} ({}ms)",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                elapsedTime);
    }
}
