package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NotProgressingStatusException extends DomainException {
    public NotProgressingStatusException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.NOT_PROGRESSING_STATUS, values);
    }

    public NotProgressingStatusException() {
        super(MeetingDomainErrorCode.NOT_PROGRESSING_STATUS);
    }
}
