package com.jolupbisang.demo.application.participationRate.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ParticipationRateErrorCode implements ErrorCode {
    PARTICIPATION_RATE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회의의 참여율 데이터가 존재하지 않습니다."),
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회의입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.");

    private final HttpStatus status;
    private final String message;
} 