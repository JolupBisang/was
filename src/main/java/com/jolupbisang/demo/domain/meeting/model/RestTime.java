package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class RestTime {
    @Column(name = "rest_interval")
    private int restInterval;

    @Column(name = "rest_duration")
    private int restDuration;

    private static final int MIN_REST_INTERVAL = 1;
    private static final int MIN_REST_DURATION = 1;

    public RestTime(int restInterval, int restDuration) {
        setRestInterval(restInterval);
        setRestDuration(restDuration);
    }

    private void setRestInterval(int restInterval) {
        if (restInterval < MIN_REST_INTERVAL) {
            throw new DomainException(MeetingDomainErrorCode.INVALID_RATE_RANGE, "restInterval: %d (최솟값: %d)", restInterval, MIN_REST_INTERVAL);
        }

        this.restInterval = restInterval;
    }

    private void setRestDuration(int restDuration) {
        if (restDuration < MIN_REST_DURATION) {
            throw new DomainException(MeetingDomainErrorCode.MINIMUM_REST_DURATION, "restDuration: %d (최솟값: %d)", restDuration, MIN_REST_DURATION);
        }

        this.restDuration = restDuration;
    }
}
