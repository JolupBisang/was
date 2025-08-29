package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class InvalidMeetingIdException extends DomainException {

    public InvalidMeetingIdException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.INVALID_MEETING_ID, values);
    }

    public InvalidMeetingIdException() {
        super(SegmentDomainErrorCode.INVALID_MEETING_ID);
    }
} 
