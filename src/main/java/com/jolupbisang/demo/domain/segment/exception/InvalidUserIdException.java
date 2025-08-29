package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class InvalidUserIdException extends DomainException {

    public InvalidUserIdException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.INVALID_USER_ID, values);
    }

    public InvalidUserIdException() {
        super(SegmentDomainErrorCode.INVALID_USER_ID);
    }
} 
