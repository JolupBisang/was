package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullStartChunkException extends DomainException {

    public NullStartChunkException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.NULL_START_CHUNK, values);
    }

    public NullStartChunkException() {
        super(SegmentDomainErrorCode.NULL_START_CHUNK);
    }
} 