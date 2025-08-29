package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptySegmentTextException extends DomainException {

    public EmptySegmentTextException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.EMPTY_SEGMENT_TEXT, values);
    }

    public EmptySegmentTextException() {
        super(SegmentDomainErrorCode.EMPTY_SEGMENT_TEXT);
    }
} 