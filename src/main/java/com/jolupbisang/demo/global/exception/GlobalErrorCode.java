package com.jolupbisang.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    INTERNAL_SERVER_ERROR("G-0001", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다. 관리자에게 문의해주세요."),
    INVALID_INPUT("G-0002", HttpStatus.UNPROCESSABLE_ENTITY, "잘못된 입력입니다."),
    EXPIRED_JWT("G-0003", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_ACCESS_TOKEN("G-0004", HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_TOKEN_SIGNATURE("G-0005", HttpStatus.UNAUTHORIZED, "잘못 서명된 토큰입니다."),
    UNKNOWN_TOKEN_ERROR("G-0006", HttpStatus.UNAUTHORIZED, "알 수 없는 토큰 에러입니다."),
    NOT_FOUND("G-0007", HttpStatus.NOT_FOUND, "없는 데이터입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
