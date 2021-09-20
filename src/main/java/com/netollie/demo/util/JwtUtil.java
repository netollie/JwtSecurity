package com.netollie.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.netollie.demo.config.properties.JwtProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Resource
    private JwtProperties jwtProperties;

    public String encode(String username) {
        LocalDateTime now = LocalDateTime.now();
        Function<LocalDateTime, Date> toDate = time -> Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        return JWT.create().withAudience(username)
            .withIssuedAt(toDate.apply(now))
            .withExpiresAt(toDate.apply(now.plusSeconds(jwtProperties.getLifespan())))
            .sign(Algorithm.HMAC256(username + jwtProperties.getSecret()));
    }

    public DecodedJWT decode(String jwt, String username) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(username + jwtProperties.getSecret())).build();
        return jwtVerifier.verify(jwt);
    }
}
