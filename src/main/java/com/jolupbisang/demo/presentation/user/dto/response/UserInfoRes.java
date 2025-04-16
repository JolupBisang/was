package com.jolupbisang.demo.presentation.user.dto.response;

import com.jolupbisang.demo.application.user.dto.UserInfo;

public record UserInfoRes(
        Long id,
        String email,
        String nickname
) {

    public static UserInfoRes from(UserInfo userInfo) {
        return new UserInfoRes(
                userInfo.id(),
                userInfo.email(),
                userInfo.nickname()
        );
    }
}
