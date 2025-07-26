package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EmptyAgendaContentException extends DomainException {
    public EmptyAgendaContentException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_AGENDA_CONTENT, values);
    }

    public EmptyAgendaContentException() {
        super(MeetingDomainErrorCode.EMPTY_AGENDA_CONTENT);
    }
}
