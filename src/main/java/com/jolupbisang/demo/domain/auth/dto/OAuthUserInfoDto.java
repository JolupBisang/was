package com.jolupbisang.demo.domain.auth.dto;

public record OAuthUserInfoDto(
        String id,
        String email,
        String name
) {
    public static OAuthUserInfoDto of(String id, String email, String name) {
        return new OAuthUserInfoDto(id, email, name);
    }
}

