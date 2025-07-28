package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyActualStartTimeException extends DomainException {

    public EmptyActualStartTimeException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.EMPTY_ACTUAL_START_TIME, values);
    }

}
