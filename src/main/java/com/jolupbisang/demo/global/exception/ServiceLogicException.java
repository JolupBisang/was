package com.jolupbisang.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ServiceLogicException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> values;

    public ServiceLogicException(ErrorCode errorCode) {
        this(errorCode, Map.of());
    }
}

