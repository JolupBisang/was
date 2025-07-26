package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class NullRestTimeException extends DomainException {

    public NullRestTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.NULL_REST_TIME, values);
    }

    public NullRestTimeException() {
        super(MeetingDomainErrorCode.NULL_REST_TIME);
    }

} 
