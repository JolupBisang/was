package com.jolupbisang.demo.meeting.domain.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class NullParticipantsException extends DomainException {

    public NullParticipantsException(List<Object> values) {
        super(MeetingDomainErrorCode.NULL_PARTICIPANTS_LIST, values);
    }

    public NullParticipantsException() {
        super(MeetingDomainErrorCode.NULL_PARTICIPANTS_LIST);
    }

} 
