package com.jolupbisang.demo.global.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class DomainException extends ServiceLogicException {

    public DomainException(ErrorCode errorCode, List<Object> values) {
        super(errorCode, values);
    }

    public DomainException(ErrorCode errorCode) {
        super(errorCode);
    }
}
