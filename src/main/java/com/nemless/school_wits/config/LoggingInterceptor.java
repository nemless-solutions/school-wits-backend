package com.nemless.school_wits.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", Instant.now());
        log.info("Method: {}; URL: {}; Remote Addr: {}; Query String: {}", request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), request.getQueryString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Instant start = (Instant) request.getAttribute("startTime");
        long duration = Instant.now().toEpochMilli() - start.toEpochMilli();

        log.info("Response Status: {}", response.getStatus());
        log.info("Time Taken: {} ms", duration);

        if (ex != null) {
            log.error("Exception: {}", ex.getMessage());
        }
    }

}
