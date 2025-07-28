package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class TooManyHostException extends DomainException {
    public TooManyHostException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.TOO_MANY_HOST, values);
    }

    public TooManyHostException() {
        super(MeetingDomainErrorCode.TOO_MANY_HOST);
    }
}
