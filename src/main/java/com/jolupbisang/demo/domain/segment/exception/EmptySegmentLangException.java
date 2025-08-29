package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptySegmentLangException extends DomainException {

    public EmptySegmentLangException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.EMPTY_SEGMENT_LANG, values);
    }

    public EmptySegmentLangException() {
        super(SegmentDomainErrorCode.EMPTY_SEGMENT_LANG);
    }
} 