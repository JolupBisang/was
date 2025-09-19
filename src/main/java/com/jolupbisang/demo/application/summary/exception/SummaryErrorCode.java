package com.jolupbisang.demo.application.summary.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SummaryErrorCode implements ErrorCode {
    NOT_PARTICIPANT("SA-0001", HttpStatus.UNAUTHORIZED, "회의의 참여자가 아닙니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
