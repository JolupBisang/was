package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private MeetingRole role;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    @Embedded
    private ParticipationRate participationRate;

    public Participant(Meeting meeting, Long userId, MeetingRole role) {
        setUserId(userId);
        initiateStatus();
        this.meeting = meeting;
        this.role = role;
    }

    public void updateParticipationRate(double rate, long totalParticipationChunk) {
        participationRate = new ParticipationRate(rate, totalParticipationChunk);
    }

    private void setUserId(long userId) {
        if (userId < 0) {
            throw new DomainException(MeetingDomainErrorCode.NEGATIVE_USER_ID, "userId: %d", userId);
        }
        this.userId = userId;
    }

    private void initiateStatus() {
        this.status = ParticipantStatus.ACCEPTED;
    }
}
