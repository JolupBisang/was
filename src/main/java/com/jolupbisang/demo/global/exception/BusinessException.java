package com.jolupbisang.demo.global.exception;

import java.util.Map;

public class BusinessException extends ServiceLogicException {

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
}
