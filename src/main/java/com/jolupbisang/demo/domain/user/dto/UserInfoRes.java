package com.jolupbisang.demo.domain.user.dto;

import com.jolupbisang.demo.domain.user.entity.User;

public record UserInfoRes(
        Long id,
        String email,
        String nickname
) {
    public static UserInfoRes from(User user) {
        return new UserInfoRes(
                user.getId(),
                user.getEmail(),
                user.getNickname());
    }
}
