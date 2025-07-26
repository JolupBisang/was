package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.DomainException;

import java.util.List;

public class MinimumRestDurationException extends DomainException {
    public MinimumRestDurationException(List<Object> values) {
        super(MeetingDomainErrorCode.MINIMUM_REST_DURATION, values);
    }
}
