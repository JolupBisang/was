package com.jolupbisang.demo.global.exception;

public class InfraException extends CustomException {

    public InfraException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public InfraException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InfraException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public InfraException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
