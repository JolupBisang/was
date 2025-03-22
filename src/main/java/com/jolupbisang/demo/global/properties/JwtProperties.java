package com.jolupbisang.demo.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("jwt")
@Getter
public class JwtProperties {
    private String secret;
    private long accessExpiration;
    private long refreshExpiration;
    private String issuer;
}
