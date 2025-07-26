package com.jolupbisang.demo.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class NullScheduledTimeException extends DomainException {
    public NullScheduledTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.NULL_SCHEDULED_TIME, values);
    }

    public NullScheduledTimeException() {
        super(MeetingDomainErrorCode.NULL_SCHEDULED_TIME);
    }
}
