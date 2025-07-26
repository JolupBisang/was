package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class NullAgendaException extends DomainException {
    public NullAgendaException(List<Object> values) {
        super(MeetingDomainErrorCode.NULL_AGENDA_LIST, values);
    }

    public NullAgendaException() {
        super(MeetingDomainErrorCode.NULL_AGENDA_LIST);
    }
}
