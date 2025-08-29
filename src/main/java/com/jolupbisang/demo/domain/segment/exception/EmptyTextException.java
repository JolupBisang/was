package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyTextException extends DomainException {

    public EmptyTextException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.EMPTY_TEXT, values);
    }

    public EmptyTextException() {
        super(SegmentDomainErrorCode.EMPTY_TEXT);
    }
} 