package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullActualProgressTimeException extends DomainException {

    public NullActualProgressTimeException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.NULL_ACTUAL_PROGRESS_TIME, values);
    }

    public NullActualProgressTimeException() {
        super(MeetingDomainErrorCode.NULL_ACTUAL_PROGRESS_TIME);
    }

} 
