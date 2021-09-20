package com.netollie.demo.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("security.jwt")
public class JwtProperties {
    private String secret;

    private long lifespan;
}
