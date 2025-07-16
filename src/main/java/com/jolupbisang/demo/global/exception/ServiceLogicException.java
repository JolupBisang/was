package com.jolupbisang.demo.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServiceLogicException extends RuntimeException {
    private final ErrorCode errorCode;
}

