package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullCreatedDateTimeException extends DomainException {
    public NullCreatedDateTimeException(Map<String, Object> values) {
        super(AudioDomainErrorCode.NULL_CREATED_DATE_TIME, values);
    }

    public NullCreatedDateTimeException() {
        super(AudioDomainErrorCode.NULL_CREATED_DATE_TIME);
    }
}
