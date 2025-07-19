package com.jolupbisang.demo.domain.meeting.entity;

import com.jolupbisang.demo.domain.meeting.exception.MinimumRestDurationException;
import com.jolupbisang.demo.domain.meeting.exception.MinimumRestIntervalException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
            throw new MinimumRestIntervalException(List.of(restInterval));
        }

        this.restInterval = restInterval;
    }

    private void setRestDuration(int restDuration) {
        if (restDuration < MIN_REST_DURATION) {
            throw new MinimumRestDurationException(List.of(restDuration));
        }

        this.restDuration = restDuration;
    }
}
