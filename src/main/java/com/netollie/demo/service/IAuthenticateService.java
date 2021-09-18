package com.netollie.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public interface IAuthenticateService
    extends UserDetailsService, AuthenticationSuccessHandler, AuthenticationFailureHandler {
}
