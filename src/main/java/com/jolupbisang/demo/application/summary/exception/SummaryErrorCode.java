package com.jolupbisang.demo.application.summary.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SummaryErrorCode implements ErrorCode {
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 회의에 대한 요약 입니다.");


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
