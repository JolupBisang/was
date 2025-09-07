package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;
import com.jolupbisang.demo.global.exception.ErrorCode;

import java.util.Map;

public class TooManyHostException extends DomainException {
    public TooManyHostException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.TOO_MANY_HOST, values);
    }

    public TooManyHostException() {
        super(MeetingDomainErrorCode.TOO_MANY_HOST);
    }

    public TooManyHostException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(errorCode, detailedMessage, args);
    }

    public TooManyHostException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(errorCode, cause, detailedMessage, args);
    }
}
