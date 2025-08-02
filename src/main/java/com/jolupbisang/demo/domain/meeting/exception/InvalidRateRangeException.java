package com.jolupbisang.demo.domain.meeting.exception;

import java.util.Map;

import com.jolupbisang.demo.global.exception.DomainException;

public class InvalidRateRangeException extends DomainException {

    public InvalidRateRangeException() {
        super(MeetingDomainErrorCode.INVALID_RATE_RANGE);
    }

    public InvalidRateRangeException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.INVALID_RATE_RANGE, values);
    }

}
