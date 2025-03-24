package com.jolupbisang.demo.application.auth.Exception;

import com.jolupbisang.demo.global.exception.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_OAUTH_PLATFORM(HttpStatus.BAD_REQUEST, "지원하지 않는 로그인 플랫폼입니다."),
    PLATFORM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "일시적인 오류입니다. 잠시 후 다시 시도해주세요."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 리프레시 토큰입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
