package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullScheduledTimeException extends DomainException {
    public NullScheduledTimeException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.NULL_SCHEDULED_TIME, values);
    }

    public NullScheduledTimeException() {
        super(MeetingDomainErrorCode.NULL_SCHEDULED_TIME);
    }
}
