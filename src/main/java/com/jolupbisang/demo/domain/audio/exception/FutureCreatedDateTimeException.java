package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class FutureCreatedDateTimeException extends DomainException {
    public FutureCreatedDateTimeException(Map<String, Object> values) {
        super(AudioDomainErrorCode.FUTURE_CREATED_DATE_TIME, values);
    }

    public FutureCreatedDateTimeException() {
        super(AudioDomainErrorCode.FUTURE_CREATED_DATE_TIME);
    }
}
