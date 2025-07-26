package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EmptyMeetingRoleException extends DomainException {
    public EmptyMeetingRoleException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_MEETING_ROLE, values);
    }

    public EmptyMeetingRoleException() {
        super(MeetingDomainErrorCode.EMPTY_MEETING_ROLE);
    }
}
