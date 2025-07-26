package com.jolupbisang.demo.application.user.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserApplicationErrorCode implements ErrorCode {
    NOT_FOUND_USER("UA-0001", HttpStatus.NOT_FOUND, "없는 회원입니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;
}
