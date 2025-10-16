package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;
import com.jolupbisang.demo.global.exception.ErrorCode;

public class TooManyHostException extends DomainException {
    public TooManyHostException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public TooManyHostException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TooManyHostException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public TooManyHostException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
