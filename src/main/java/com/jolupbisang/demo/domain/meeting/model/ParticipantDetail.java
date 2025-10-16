package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.MeetingDomainErrorCode;
import com.jolupbisang.demo.global.exception.DomainException;
import lombok.Getter;

@Getter
public class ParticipantDetail {
    private Long userId;
    private MeetingRole meetingRole;

    public ParticipantDetail(Long userId, MeetingRole meetingRole) {
        setUserId(userId);
        setRole(meetingRole);
    }

    private void setUserId(long userId) {
        this.userId = userId;
    }

    private void setRole(MeetingRole meetingRole) {
        if (meetingRole == null) {
            throw new DomainException(MeetingDomainErrorCode.EMPTY_MEETING_ROLE);
        }
        this.meetingRole = meetingRole;
    }
}
