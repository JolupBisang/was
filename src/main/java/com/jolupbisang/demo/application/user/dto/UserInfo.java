package com.jolupbisang.demo.application.user.dto;

import com.jolupbisang.demo.domain.user.User;

public record UserInfo(
        Long id,
        String email,
        String nickname
) {
    public static UserInfo fromEntity(User user) {
        return new UserInfo(
                user.getId(),
                user.getEmail(),
                user.getNickname());
    }
}
