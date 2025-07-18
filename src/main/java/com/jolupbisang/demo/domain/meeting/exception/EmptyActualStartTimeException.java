package com.jolupbisang.demo.domain.meeting.exception;

import java.util.List;

import com.jolupbisang.demo.global.exception.DomainException;

public class EmptyActualStartTimeException extends DomainException{

    public EmptyActualStartTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_ACTUAL_START_TIME, values);
    }

}
