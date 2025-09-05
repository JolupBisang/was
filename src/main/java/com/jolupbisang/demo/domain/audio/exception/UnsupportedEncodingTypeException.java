package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class UnsupportedEncodingTypeException extends DomainException {
    public UnsupportedEncodingTypeException(Map<String, Object> values) {
        super(AudioDomainErrorCode.UNSUPPORTED_ENCODING_TYPE, values);
    }

    public UnsupportedEncodingTypeException() {
        super(AudioDomainErrorCode.UNSUPPORTED_ENCODING_TYPE);
    }
}
