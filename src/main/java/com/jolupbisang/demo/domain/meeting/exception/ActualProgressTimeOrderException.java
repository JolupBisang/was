package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.Map;

public class ActualProgressTimeOrderException extends DomainException {

    public ActualProgressTimeOrderException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.ACTUAL_PROGRESS_TIME_ORDER_EXCEPTION, values);
    }

}
