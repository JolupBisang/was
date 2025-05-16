package com.jolupbisang.demo.application.common.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingAccessErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회의입니다."),
    NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "진행중인 회의가 아닙니다."),
    NOT_PARTICIPANT(HttpStatus.FORBIDDEN, "해당 회의의 참여자가 아닙니다."),
    NOT_LEADER(HttpStatus.FORBIDDEN, "해당 작업은 회의 리더만 수행할 수 있습니다.");

    private final HttpStatus status;
    private final String message;
}
