package com.jolupbisang.demo.domain.auth.dto;

import com.jolupbisang.demo.domain.user.entity.OAuthPlatform;
import com.jolupbisang.demo.domain.user.entity.User;

public record OAuthUserInfoDto(
        String id,
        String email,
        String name
) {
    public static OAuthUserInfoDto of(String id, String email, String name) {
        return new OAuthUserInfoDto(id, email, name);
    }

    public User toEntity(OAuthPlatform platform) {
        return User.builder()
                .email(email)
                .nickname(name)
                .platform(platform)
                .build();
    }
}

