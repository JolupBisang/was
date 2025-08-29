package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullSpokenDateTimeException extends DomainException {

    public NullSpokenDateTimeException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.NULL_SPOKEN_DATE_TIME, values);
    }

    public NullSpokenDateTimeException() {
        super(SegmentDomainErrorCode.NULL_SPOKEN_DATE_TIME);
    }
} 