package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EndTimeBeforeStartTimeException extends DomainException {
    public EndTimeBeforeStartTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.END_TIME_BEFORE_START_TIME, values);
    }
}
