package com.jolupbisang.demo.global.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DomainException extends ServiceLogicException {

    public DomainException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode, values);
    }

    public DomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
