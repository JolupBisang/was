package com.jolupbisang.demo.global.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(Throwable cause) {
        super(GlobalErrorCode.NOT_FOUND, cause);
    }

    public NotFoundException() {
        super(GlobalErrorCode.NOT_FOUND);
    }

    public NotFoundException(String detailedMessage, Object... args) {
        super(GlobalErrorCode.NOT_FOUND, detailedMessage, args);
    }

    public NotFoundException(Throwable cause, String detailedMessage, Object... args) {
        super(GlobalErrorCode.NOT_FOUND, cause, detailedMessage, args);
    }
}
