package com.netollie.demo.service;

import com.netollie.demo.constants.AuthenticateConstant;
import com.netollie.demo.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Service
public class AuthenticateService implements IAuthenticateService {
    @Resource
    private JwtUtil jwtUtil;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String encodedPassword = "$2a$10$s4KsHrMQMjMUaTdr.WtAO.slxbCaJdMg2JyyLsAFOx5q/25TCgcua";
        return new UserDetails(username, encodedPassword);
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        String jwt = jwtUtil.encode(username);
        httpServletResponse.addCookie(new Cookie(AuthenticateConstant.COOKIE_KEY_JWT, jwt));
        httpServletResponse.addCookie(new Cookie(AuthenticateConstant.COOKIE_KEY_USERNAME, username));
    }

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse,
        AuthenticationException e) throws IOException, ServletException {
        System.out.println("登录失败");
    }

    /**
     * User Details
     */
    @AllArgsConstructor
    private static class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        public boolean isAccountNonExpired() {
            return true;
        }

        public boolean isAccountNonLocked() {
            return true;
        }

        public boolean isCredentialsNonExpired() {
            return true;
        }

        public boolean isEnabled() {
            return true;
        }
    };
}
