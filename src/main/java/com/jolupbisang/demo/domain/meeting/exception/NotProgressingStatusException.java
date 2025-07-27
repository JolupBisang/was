package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class NotProgressingStatusException extends DomainException {
    public NotProgressingStatusException(List<Object> values) {
        super(MeetingDomainErrorCode.NOT_PROGRESSING_STATUS, values);
    }

    public NotProgressingStatusException() {
        super(MeetingDomainErrorCode.NOT_PROGRESSING_STATUS);
    }
}
