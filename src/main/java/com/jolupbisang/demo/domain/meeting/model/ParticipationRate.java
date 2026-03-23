package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class ParticipationRate {

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private Long totalParticipationChunk;

    private static final double MIN_RATE = 0;
    private static final double MAX_RATE = 1;
    private static final long MIN_TOTAL_PARTICIPATION_CHUNK = 0;
    private static final double DEFAULT_RATE = 0;
    private static final long DEFAULT_TOTAL_PARTICIPATION_CHUNK = 0;


    public ParticipationRate() {
        this.rate = DEFAULT_RATE;
        this.totalParticipationChunk = DEFAULT_TOTAL_PARTICIPATION_CHUNK;
    }

    public ParticipationRate(double rate, long totalParticipationChunk) {
        setRate(rate);
        setTotalParticipationChunk(totalParticipationChunk);
    }

    private void setRate(double rate) {
        if (rate < MIN_RATE || rate > MAX_RATE) {
            throw new DomainException(MeetingDomainErrorCode.INVALID_RATE_RANGE, "participation rate: %f", rate);
        }
        this.rate = rate;
    }

    private void setTotalParticipationChunk(long totalParticipationChunk) {
        if (totalParticipationChunk < MIN_TOTAL_PARTICIPATION_CHUNK) {
            throw new DomainException(MeetingDomainErrorCode.INVALID_TOTAL_PARTICIPATION_CHUNK, "total participation chunk: %d (최솟값: %d)", totalParticipationChunk, MIN_TOTAL_PARTICIPATION_CHUNK);
        }
        this.totalParticipationChunk = totalParticipationChunk;
    }
}
