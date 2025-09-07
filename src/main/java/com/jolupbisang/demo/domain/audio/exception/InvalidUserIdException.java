package com.jolupbisang.demo.domain.audio.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class InvalidUserIdException extends DomainException {
    public InvalidUserIdException(Map<String, Object> values) {
        super(AudioDomainErrorCode.NEGATIVE_USER_ID, values);
    }

    public InvalidUserIdException() {
        super(AudioDomainErrorCode.NEGATIVE_USER_ID);
    }
}
