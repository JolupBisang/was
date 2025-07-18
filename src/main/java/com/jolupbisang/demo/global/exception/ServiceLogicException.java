package com.jolupbisang.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ServiceLogicException extends RuntimeException {
    private final ErrorCode errorCode;
    private final List<Object> values;

    public ServiceLogicException(ErrorCode errorCode) {
        this(errorCode, List.of());
    }
}

