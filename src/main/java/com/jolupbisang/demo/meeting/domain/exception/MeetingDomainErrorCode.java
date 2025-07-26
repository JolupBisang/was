package com.jolupbisang.demo.meeting.domain.exception;

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
    NOT_PROGRESSING_STATUS("MD-0009", HttpStatus.BAD_REQUEST, "진행중인 회의가 아닙니다."),
    EMPTY_TITLE("MD-0010", HttpStatus.BAD_REQUEST, "회의 이름은 필수입니다."),
    EMPTY_LOCATION("MD-0011", HttpStatus.BAD_REQUEST, "회의 장소는 필수입니다."),
    NULL_SCHEDULED_TIME("MD-0012", HttpStatus.BAD_REQUEST, "회의 시간은 필수입니다."),
    NULL_ACTUAL_PROGRESS_TIME("MD-0013", HttpStatus.BAD_REQUEST, "실제 회의 시간 정보는 필수입니다."),
    NULL_REST_TIME("MD-0014", HttpStatus.BAD_REQUEST, "회의 시는시간 정보는 필수이빈다."),
    NULL_PARTICIPANTS_LIST("MD-0016", HttpStatus.BAD_REQUEST, "참여자 목록은 필수입니다."),
    EMPTY_PARTICIPANT_ID("MD-0017", HttpStatus.BAD_REQUEST, "참여자의 정보는 필수입니다."),
    NULL_AGENDA_LIST("MD-0018", HttpStatus.BAD_REQUEST, "아젠다 목록은 필수입니다."),
    TOO_MANY_HOST("MD-0019", HttpStatus.BAD_REQUEST, "회의 메인 호스트는 한명이어야 합니다."),
    EMPTY_MEETING_ROLE("MD-0020", HttpStatus.BAD_REQUEST, "회의에서 회원의 역할은 필수입니다."),
    EMPTY_AGENDA_CONTENT("MD-0021", HttpStatus.BAD_REQUEST, "회의 안건 내용이 비어있습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
