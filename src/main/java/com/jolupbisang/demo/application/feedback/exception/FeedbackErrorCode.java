package com.jolupbisang.demo.application.feedback.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FeedbackErrorCode implements ErrorCode {
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 회의에 대한 피드백 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 회원에 대한 피드백입니다.");


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
