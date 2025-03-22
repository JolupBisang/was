package com.jolupbisang.demo.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthPlatform {
    GOOGLE("GOOGLE");

    private final String name;
}
