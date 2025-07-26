package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class InvalidHostCountException extends DomainException {
    public InvalidHostCountException(List<Object> values) {
        super(MeetingDomainErrorCode.INVALID_HOST_COUNT, values);
    }

    public InvalidHostCountException() {
        super(MeetingDomainErrorCode.INVALID_HOST_COUNT);
    }
}
