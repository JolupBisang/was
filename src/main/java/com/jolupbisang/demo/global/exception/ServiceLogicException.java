package com.jolupbisang.demo.global.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ServiceLogicException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> values;

    public ServiceLogicException(ErrorCode errorCode, Map<String, Object> values, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.values = values;
    }

    public ServiceLogicException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.values = values;
    }

    public ServiceLogicException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.values = Map.of();
    }

    public ServiceLogicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.values = Map.of();
    }
}

