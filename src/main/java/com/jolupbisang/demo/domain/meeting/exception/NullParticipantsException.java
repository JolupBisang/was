package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class NullParticipantsException extends DomainException {

    public NullParticipantsException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.NULL_PARTICIPANTS_LIST, values);
    }

    public NullParticipantsException() {
        super(MeetingDomainErrorCode.NULL_PARTICIPANTS_LIST);
    }

} 
