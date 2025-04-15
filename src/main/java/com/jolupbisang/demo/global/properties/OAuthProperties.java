package com.jolupbisang.demo.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("oauth2")
@Component
public class OAuthProperties {
    private Platform google;

    @Getter
    @RequiredArgsConstructor
    public static class Platform {
        private final String tokenUri;
        private final String userInfoUri;
        private final String clientId;
        private final String clientSecret;
        private final RedirectUris redirectUris;

        @Getter
        @RequiredArgsConstructor
        public static class RedirectUris {
            private final String web;
            private final String app;
        }
    }
}
