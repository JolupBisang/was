package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingApplicationErrorCode implements ErrorCode {
    NOT_FOUND_MEETING("MA-0001", HttpStatus.NOT_FOUND, "없는 회의 입니다."),
    NOT_PARTICIPANT("MA-0002", HttpStatus.UNAUTHORIZED, "회의 참여자가 아닙니다."),
    INVALID_QUERY_DATE("MA-0003", HttpStatus.BAD_REQUEST, "잘못된 형식의 날짜입니다."),
    NO_SUCH_MEETING_STATUS("MA-0004", HttpStatus.BAD_REQUEST, "해당 회의 상태로는 변경할 수 없습니다. (없는 상태일 수 있음)"),
    NOT_IN_PROGRESS_MEETING("MA-0005", HttpStatus.FORBIDDEN, "회의가 진행중 상태일때만 가능한 동작입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
