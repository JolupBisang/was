package com.jolupbisang.demo.application.meeting.exception;

import com.jolupbisang.demo.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AudioError implements ErrorCode {
    INVALID_META_DATA(HttpStatus.BAD_REQUEST, "잘못된 메타 데이터 형식입니다.");

    private final HttpStatus status;
    private final String message;
}
