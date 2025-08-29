package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class InvalidOrderException extends DomainException {

    public InvalidOrderException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.INVALID_ORDER, values);
    }

    public InvalidOrderException() {
        super(SegmentDomainErrorCode.INVALID_ORDER);
    }
} 
