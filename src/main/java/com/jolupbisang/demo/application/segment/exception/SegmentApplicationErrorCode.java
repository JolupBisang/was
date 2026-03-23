package com.jolupbisang.demo.application.segment.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SegmentApplicationErrorCode implements ErrorCode {
    NOT_PARTICIPANT("SA-0001", HttpStatus.UNAUTHORIZED, "회의 참가자가 아닙니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
