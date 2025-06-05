package com.jolupbisang.demo.application.agenda.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AgendaErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다."),
    MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회의입니다."),
    CANNOT_ADD_AGENDA(HttpStatus.BAD_REQUEST, "완료되었거나 취소된 회의에는 안건을 추가할 수 없습니다."),
    CANNOT_DELETE_AGENDA(HttpStatus.BAD_REQUEST, "대기 중인 회의에서만 안건을 삭제할 수 있습니다."), 
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 안건입니다.")
    ;

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
