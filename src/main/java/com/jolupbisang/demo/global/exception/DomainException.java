package com.jolupbisang.demo.global.exception;

import lombok.Getter;

@Getter
public class DomainException extends CustomException {

    public DomainException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public DomainException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DomainException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public DomainException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
