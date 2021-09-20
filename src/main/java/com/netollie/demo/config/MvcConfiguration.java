package com.netollie.demo.config;

import com.netollie.demo.interceptor.JwtInterceptor;
import com.netollie.demo.util.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(new JwtInterceptor(jwtUtil)).addPathPatterns("/**");
    }
}
