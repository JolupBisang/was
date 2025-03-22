package com.jolupbisang.demo.domain.auth.service;

import com.jolupbisang.demo.domain.auth.service.client.GoogleClient;
import com.jolupbisang.demo.domain.auth.service.client.OAuthClient;
import com.jolupbisang.demo.domain.user.entity.OAuthPlatform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class OAuthClientFactory {
    private final GoogleClient googleClient;

    public Optional<OAuthClient> getClientByPlatform(OAuthPlatform platform) {
        return switch (platform) {
            case GOOGLE -> Optional.of(googleClient);
            default -> Optional.empty();
        };
    }
}
