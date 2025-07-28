package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class StartTimeNullException extends DomainException {
    public StartTimeNullException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.EMPTY_START_TIME, values);
    }

    public StartTimeNullException() {
        super(MeetingDomainErrorCode.EMPTY_START_TIME);
    }
}
