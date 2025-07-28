package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyLocationException extends DomainException {

    public EmptyLocationException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.EMPTY_LOCATION, values);
    }

    public EmptyLocationException() {
        super(MeetingDomainErrorCode.EMPTY_LOCATION);
    }

} 
