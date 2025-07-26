package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EmptyEndTimeException extends DomainException {

    public EmptyEndTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_END_TIME, values);
    }
}
