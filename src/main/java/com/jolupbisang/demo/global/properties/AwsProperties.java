package com.jolupbisang.demo.global.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cloud.aws")
@Getter
@Setter
public class AwsProperties {

    private Credentials credentials;
    private String region;

    @Getter
    @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }
} 
