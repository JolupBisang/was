package com.jolupbisang.demo.infrastructure.user.dto;

public record UserSummary(
        long id,
        String nickname
) {
    public Object di() {
        return null;
    }
}
