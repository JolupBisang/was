package com.jolupbisang.demo.global.exception;

import java.util.Map;

public class BusinessException extends CustomException {

    // 기존 Map 방식 (하위호환성)
    public BusinessException(ErrorCode errorCode, Map<String, Object> values, Throwable cause) {
        super(errorCode, values, cause);
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode, values);
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    // 새로운 String.format 방식
    public BusinessException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public BusinessException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
