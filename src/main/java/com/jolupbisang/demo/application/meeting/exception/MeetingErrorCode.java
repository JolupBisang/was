package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "잘못된 회의 날짜 형식입니다."),
    MEETING_NOT_WAITING(HttpStatus.BAD_REQUEST, "대기 중인 회의가 아닙니다."),
    MEETING_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "진행 중인 회의가 아닙니다."),
    CANNOT_CHANGE_TO_REQUESTED_STATUS(HttpStatus.BAD_REQUEST, "요청된 상태로 회의를 변경할 수 없거나 유효하지 않은 상태 값입니다.");

    private final HttpStatus status;
    private final String message;
}
