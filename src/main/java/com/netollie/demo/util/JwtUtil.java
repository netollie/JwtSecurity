package com.netollie.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Function;

public final class JwtUtil {

    private static final String JWT_SECRET = "c3a4f325d8282f93c03dfb552d8f358f";

    private static final long JWT_LIFESPAN = 60L;

    private static final String COOKIE_JWT = "JWT";

    private static final String COOKIE_USERNAME = "USERNAME";

    public static Cookie[] generate(String username) {
        LocalDateTime now = LocalDateTime.now();
        Function<LocalDateTime, Date> toDate = time -> Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        String jwt = JWT.create().withAudience(username)
            .withIssuedAt(toDate.apply(now))
            .withExpiresAt(toDate.apply(now.plusSeconds(JWT_LIFESPAN)))
            .sign(Algorithm.HMAC256(username + JWT_SECRET));
        return new Cookie[]{
            new Cookie(COOKIE_JWT, jwt),
            new Cookie(COOKIE_USERNAME, username)
        };
    }

    public static DecodedJWT decode(Cookie[] cookies) {
        Function<String, String> getCookieValue = key -> Arrays.stream(cookies)
            .filter(cookie -> key.equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst()
            .orElse("");
        String username = getCookieValue.apply(COOKIE_USERNAME);
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(username + JWT_SECRET)).build();
        String jwt = getCookieValue.apply(COOKIE_JWT);
        return jwtVerifier.verify(jwt);
    }

    private JwtUtil() {
        // do nothing
    }
}
