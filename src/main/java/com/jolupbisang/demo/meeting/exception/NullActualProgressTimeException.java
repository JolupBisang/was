package com.jolupbisang.demo.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class NullActualProgressTimeException extends DomainException {

    public NullActualProgressTimeException(List<Object> values) {
        super(MeetingDomainErrorCode.NULL_ACTUAL_PROGRESS_TIME, values);
    }

    public NullActualProgressTimeException() {
        super(MeetingDomainErrorCode.NULL_ACTUAL_PROGRESS_TIME);
    }

} 
