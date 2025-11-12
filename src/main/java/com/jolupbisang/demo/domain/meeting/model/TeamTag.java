package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team_tag")
public class TeamTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    public TeamTag(Meeting meeting, Long teamId) {
        setMeeting(meeting);
        setTeamId(teamId);
    }

    private void setMeeting(Meeting meeting) {
        if (meeting == null) {
            throw new DomainException(MeetingDomainErrorCode.NULL_TEAM);
        }
        this.meeting = meeting;
    }

    private void setTeamId(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new DomainException(MeetingDomainErrorCode.NULL_TEAM);
        }
        this.teamId = teamId;
    }
}

