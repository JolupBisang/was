package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class InvalidSegmentChunkOrderException extends DomainException {

    public InvalidSegmentChunkOrderException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.INVALID_SEGMENT_CHUNK_ORDER, values);
    }

    public InvalidSegmentChunkOrderException() {
        super(SegmentDomainErrorCode.INVALID_SEGMENT_CHUNK_ORDER);
    }
} 