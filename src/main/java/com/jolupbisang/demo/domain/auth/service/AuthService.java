package com.jolupbisang.demo.domain.auth.service;

import com.jolupbisang.demo.domain.auth.Exception.AuthErrorCode;
import com.jolupbisang.demo.domain.auth.dto.OAuthUserInfoDto;
import com.jolupbisang.demo.domain.auth.service.client.OAuthClient;
import com.jolupbisang.demo.domain.user.entity.OAuthPlatform;
import com.jolupbisang.demo.domain.user.entity.User;
import com.jolupbisang.demo.domain.user.repository.UserRepository;
import com.jolupbisang.demo.global.common.exception.CustomException;
import com.jolupbisang.demo.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthClientFactory oAuthClientFactory;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String loginWithOAuth(OAuthPlatform platform, String code) {
        OAuthClient oAuthClient = oAuthClientFactory.getClientByPlatform(platform)
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_OAUTH_PLATFORM));

        String OAuthAccessToken = oAuthClient.requestAccessToken(code);
        OAuthUserInfoDto oAuthUserInfoDto = oAuthClient.requestUserInfo(OAuthAccessToken);

        //이미 가입된 회원 확인
        User user = userRepository.findByEmail(oAuthUserInfoDto.email())
                .orElseGet(() -> userRepository.save(oAuthUserInfoDto.toEntity(platform)));

        return jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getNickname());
    }
}
