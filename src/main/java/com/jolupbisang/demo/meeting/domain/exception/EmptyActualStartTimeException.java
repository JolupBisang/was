package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EmptyActualStartTimeException extends DomainException {

    public EmptyActualStartTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_ACTUAL_START_TIME, values);
    }

}
