package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MeetingDomainErrorCode implements ErrorCode {
    MINIMUM_REST_INTERVAL("MD-0001", HttpStatus.BAD_REQUEST, "쉬는시간 간격은 0보다 커야합니다."),
    MINIMUM_REST_DURATION("MD-0002", HttpStatus.BAD_REQUEST, "쉬는시간 지속시간은 0보다 커야합니다."),
    EMPTY_START_TIME("MD-0003", HttpStatus.BAD_REQUEST, "시작시간은 필수입니다."),
    EMPTY_END_TIME("MD-0004", HttpStatus.BAD_REQUEST, "종료시간을 필수입니다."),
    END_TIME_BEFORE_START_TIME("MD-0005", HttpStatus.BAD_REQUEST, "시작시간은 종료시간보다 빨라야합니다."),
    ACTUAL_PROGRESS_TIME_ORDER_EXCEPTION("MD-0006", HttpStatus.BAD_REQUEST, "시작시간은 종료시간보다 빨라야합니다."),
    EMPTY_ACTUAL_START_TIME("MD-0007", HttpStatus.BAD_REQUEST, "시작되지 않은 회의 입니다."),
    NOT_WAITING_STATUS("MD-0008", HttpStatus.BAD_REQUEST, "대기상태의 회의가 아닙니다."),
    NOT_PROGRESSING_STATUS("MD-0009", HttpStatus.BAD_REQUEST, "진행중인 회의가 아닙니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
