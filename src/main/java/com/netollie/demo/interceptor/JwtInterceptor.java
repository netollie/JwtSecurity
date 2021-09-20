package com.netollie.demo.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.netollie.demo.annotation.LoginRequired;
import com.netollie.demo.constants.AuthenticateConstant;
import com.netollie.demo.object.bo.UserBO;
import com.netollie.demo.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

@AllArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private static final ThreadLocal<UserBO> USER_THREAD_LOCAL = new ThreadLocal<>();

    private JwtUtil jwtUtil;

    public static UserBO getUser() {
        return USER_THREAD_LOCAL.get();
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            LoginRequired loginRequired = method.getMethodAnnotation(LoginRequired.class);
            if (Objects.nonNull(loginRequired)) {
                Cookie[] cookies = Objects.nonNull(request.getCookies()) ? request.getCookies() : new Cookie[0];
                Function<String, String> getCookieValue = key -> Arrays.stream(cookies)
                    .filter(cookie -> key.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("");
                String jwt = getCookieValue.apply(AuthenticateConstant.COOKIE_KEY_JWT);
                String username = getCookieValue.apply(AuthenticateConstant.COOKIE_KEY_USERNAME);
                try {
                    DecodedJWT decodedJWT = jwtUtil.decode(jwt, username);
                    USER_THREAD_LOCAL.set(UserBO.of(decodedJWT));
                } catch (Exception e) {
                    response.getWriter().write("authenticate failed");
                    return false;
                }
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        USER_THREAD_LOCAL.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
