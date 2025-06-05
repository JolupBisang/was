package com.jolupbisang.demo.application.meetingUser.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MeetingUserErrorCode implements ErrorCode {
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회의입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_ALREADY_PARTICIPANT(HttpStatus.BAD_REQUEST, "이미 회의에 참여 중인 사용자입니다."),
    USER_NOT_PARTICIPANT(HttpStatus.BAD_REQUEST, "회의에 참여하지 않은 사용자입니다."),
    CANNOT_REMOVE_HOST(HttpStatus.BAD_REQUEST, "호스트는 회의에서 제거할 수 없습니다."),
    CANNOT_ADD_TO_COMPLETED_MEETING(HttpStatus.BAD_REQUEST, "완료되었거나 취소된 회의에는 참여자를 추가할 수 없습니다."),
    CANNOT_REMOVE_FROM_NON_WAITING_MEETING(HttpStatus.BAD_REQUEST, "대기 중인 회의에서만 참여자를 제거할 수 있습니다.");

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