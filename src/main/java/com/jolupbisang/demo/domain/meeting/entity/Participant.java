package com.jolupbisang.demo.domain.meeting.entity;

import com.jolupbisang.demo.domain.meeting.exception.EmptyParticipantIdException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Participant {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "is_host")
    private boolean isHost;

    @Enumerated(EnumType.STRING)
    private MeetingUserStatus status;

    public Participant(Long userId, boolean isHost, MeetingUserStatus status) {
        setUserId(userId);
        this.userId = userId;
        this.isHost = isHost;
        this.status = status;
    }

    private void setUserId(Long userId) {
        if (userId == null) {
            throw new EmptyParticipantIdException();
        }
    }
}
