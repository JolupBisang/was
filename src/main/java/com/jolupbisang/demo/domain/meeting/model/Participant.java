package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.EmptyParticipantIdException;
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

    public Participant(Meeting meeting, Long userId, MeetingRole role) {
        setUserId(userId);
        initiateStatus();
        this.meeting = meeting;
        this.role = role;
    }

    private void setUserId(Long userId) {
        if (userId == null) {
            throw new EmptyParticipantIdException();
        }
        this.userId = userId;
    }

    private void initiateStatus() {
        this.status = ParticipantStatus.ACCEPTED;
    }
}
