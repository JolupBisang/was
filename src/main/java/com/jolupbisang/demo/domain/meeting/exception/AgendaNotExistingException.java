package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class AgendaNotExistingException extends DomainException {
    public AgendaNotExistingException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.NON_EXISTING_AGENDA, values);
    }

    public AgendaNotExistingException() {
        super(MeetingDomainErrorCode.NON_EXISTING_AGENDA);
    }
}
