package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class EmptyTitleException extends DomainException {

    public EmptyTitleException(List<Object> values) {
        super(MeetingDomainErrorCode.EMPTY_TITLE, values);
    }

    public EmptyTitleException() {
        super(MeetingDomainErrorCode.EMPTY_TITLE);
    }

} 
