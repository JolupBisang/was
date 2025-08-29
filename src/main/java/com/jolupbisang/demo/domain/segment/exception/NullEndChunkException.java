package com.jolupbisang.demo.domain.segment.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullEndChunkException extends DomainException {

    public NullEndChunkException(Map<String, Object> values) {
        super(SegmentDomainErrorCode.NULL_END_CHUNK, values);
    }

    public NullEndChunkException() {
        super(SegmentDomainErrorCode.NULL_END_CHUNK);
    }
} 