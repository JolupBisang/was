package com.jolupbisang.demo.application.auth.service;

import com.jolupbisang.demo.application.auth.dto.OAuthUserInfoDto;
import com.jolupbisang.demo.application.auth.exception.AuthErrorCode;
import com.jolupbisang.demo.domain.user.OAuthPlatform;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.auth.JwtProvider;
import com.jolupbisang.demo.infrastructure.auth.OAuthClientFactory;
import com.jolupbisang.demo.infrastructure.auth.client.OAuthClient;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OAuthClientFactory oAuthClientFactory;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public String loginWithOAuth(OAuthPlatform oAuthPlatform, ClientPlatform clientPlatform, String code) {
        OAuthClient oAuthClient = oAuthClientFactory.getClientByPlatform(oAuthPlatform)
                .orElseThrow(() -> new CustomException(AuthErrorCode.INVALID_OAUTH_PLATFORM));

        String OAuthAccessToken = oAuthClient.requestAccessToken(clientPlatform, code);
        OAuthUserInfoDto oAuthUserInfoDto = oAuthClient.requestUserInfo(OAuthAccessToken);

        //이미 가입된 회원 확인
        User user = userRepository.findByEmail(oAuthUserInfoDto.email())
                .orElseGet(() -> userRepository.save(oAuthUserInfoDto.toEntity(oAuthPlatform)));

        return jwtProvider.generateAccessToken(user.getId(), user.getEmail(), user.getNickname());
    }
}
