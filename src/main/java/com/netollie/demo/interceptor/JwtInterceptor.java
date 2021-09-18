package com.netollie.demo.interceptor;

import com.netollie.demo.annotation.LoginRequired;
import com.netollie.demo.util.JwtUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        LoginRequired loginRequired = method.getMethodAnnotation(LoginRequired.class);
        if (Objects.nonNull(loginRequired)) {
            try {
                JwtUtil.decode(request.getCookies());
            } catch (Exception e) {
                response.getWriter().write("authenticate failed");
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
