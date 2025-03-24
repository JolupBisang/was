package com.jolupbisang.demo.infrastructure.auth;

import com.jolupbisang.demo.domain.user.entity.OAuthPlatform;
import com.jolupbisang.demo.infrastructure.auth.client.GoogleClient;
import com.jolupbisang.demo.infrastructure.auth.client.OAuthClient;
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
