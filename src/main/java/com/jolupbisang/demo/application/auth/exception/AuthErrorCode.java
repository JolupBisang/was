package com.jolupbisang.demo.application.auth.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_OAUTH_PLATFORM("AUA-0001", HttpStatus.BAD_REQUEST, "지원하지 않는 로그인 플랫폼입니다."),
    PLATFORM_ERROR("AUA-0002", HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 오류입니다. 잠시 후 다시 시도해주세요."),
    REFRESH_TOKEN_NOT_FOUND("AUA-0003", HttpStatus.BAD_REQUEST, "존재하지 않는 리프레시 토큰입니다."),
    INVALID_CLIENT_PLATFORM("AUA-0004", HttpStatus.BAD_REQUEST, "존재하지 않는 클라이언트 플랫폼입니다."),
    ;

    private final String code;
    private final HttpStatus status;
    private final String message;
}
