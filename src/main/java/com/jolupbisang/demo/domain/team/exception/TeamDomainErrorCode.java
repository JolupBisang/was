package com.jolupbisang.demo.domain.team.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamDomainErrorCode implements ErrorCode {
    INVALID_TEAM_NAME("TD-0001", HttpStatus.BAD_REQUEST, "잘못된 팀 이름입니다."),
    NULL_TEAM_NAME("TD-0002", HttpStatus.BAD_REQUEST, "팀 이름은 필수 입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
