package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyEndTimeException extends DomainException {

    public EmptyEndTimeException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.EMPTY_END_TIME, values);
    }
}
