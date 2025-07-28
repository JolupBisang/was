package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyAgendaContentException extends DomainException {
    public EmptyAgendaContentException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.EMPTY_AGENDA_CONTENT, values);
    }

    public EmptyAgendaContentException() {
        super(MeetingDomainErrorCode.EMPTY_AGENDA_CONTENT);
    }
}
