package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class ActualProgressTimeOrderException extends DomainException {

    public ActualProgressTimeOrderException(List<Object> values) {
        super(MeetingDomainErrorCode.ACTUAL_PROGRESS_TIME_ORDER_EXCEPTION, values);
    }

}
