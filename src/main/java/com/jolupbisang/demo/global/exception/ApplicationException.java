package com.jolupbisang.demo.global.exception;

public class ApplicationException extends CustomException {

    public ApplicationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApplicationException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public ApplicationException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
