package com.jolupbisang.demo.domain.team.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TeamDomainErrorCode implements ErrorCode {
    INVALID_TEAM_NAME("TD-0001", HttpStatus.BAD_REQUEST, "잘못된 팀 이름입니다."),
    NULL_TEAM_NAME("TD-0002", HttpStatus.BAD_REQUEST, "팀 이름은 필수 입니다."),
    NULL_TEAM("TD-0003", HttpStatus.BAD_REQUEST, "팀은 필수입니다."),
    INVALID_USER_ID("TD-0004", HttpStatus.BAD_REQUEST, "사용자 ID는 0보다 커야 합니다."),
    NULL_TEAM_MEMBER_ROLE("TD-0005", HttpStatus.BAD_REQUEST, "팀 멤버 역할은 필수입니다."),
    EMPTY_TEAM_MEMBERS("TD-0006", HttpStatus.BAD_REQUEST, "팀 멤버는 최소 1명 이상이어야 합니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
}
