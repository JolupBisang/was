package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "진행중인 회의가 아닙니다.");

    private final HttpStatus status;
    private final String message;
}
