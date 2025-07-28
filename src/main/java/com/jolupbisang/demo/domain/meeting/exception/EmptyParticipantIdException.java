package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class EmptyParticipantIdException extends DomainException {
    public EmptyParticipantIdException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.EMPTY_PARTICIPANT_ID, values);
    }

    public EmptyParticipantIdException() {
        super(MeetingDomainErrorCode.EMPTY_PARTICIPANT_ID);
    }
}
