package com.jolupbisang.demo.global.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DomainException extends CustomException {

    // 기존 Map 방식 (하위호환성)
    public DomainException(ErrorCode errorCode, Map<String, Object> values, Throwable cause) {
        super(errorCode, values, cause);
    }

    public DomainException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode, values);
    }

    public DomainException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public DomainException(ErrorCode errorCode) {
        super(errorCode);
    }

    // 새로운 String.format 방식
    public DomainException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public DomainException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
