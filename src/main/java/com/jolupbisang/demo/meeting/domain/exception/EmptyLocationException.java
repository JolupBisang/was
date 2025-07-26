package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EmptyLocationException extends DomainException {

    public EmptyLocationException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_LOCATION, values);
    }

    public EmptyLocationException() {
        super(MeetingDomainErrorCode.EMPTY_LOCATION);
    }

} 
