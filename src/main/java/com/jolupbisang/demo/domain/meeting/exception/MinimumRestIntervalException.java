package com.jolupbisang.demo.domain.meeting.exception;

import com.jolupbisang.demo.global.exception.BusinessException;

import java.util.Map;

public class MinimumRestIntervalException extends BusinessException {

    public MinimumRestIntervalException(Map<String, Object> values) {
        super(MeetingDomainErrorCode.MINIMUM_REST_INTERVAL, values);
    }
}
