package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullAgendaException extends DomainException {
    public NullAgendaException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.NULL_AGENDA_LIST, values);
    }

    public NullAgendaException() {
        super(MeetingDomainErrorCode.NULL_AGENDA_LIST);
    }
}
