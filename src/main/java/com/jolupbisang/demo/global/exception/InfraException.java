package com.jolupbisang.demo.global.exception;

import java.util.Map;

public class InfraException extends ServiceLogicException {

    public InfraException(ErrorCode errorCode, Map<String, Object> values, Throwable cause) {
        super(errorCode, values, cause);
    }

    public InfraException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode, values);
    }

    public InfraException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public InfraException(ErrorCode errorCode) {
        super(errorCode);
    }
}
