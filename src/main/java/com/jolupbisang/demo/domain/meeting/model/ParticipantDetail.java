package com.jolupbisang.demo.domain.meeting.model;

import com.jolupbisang.demo.domain.meeting.exception.EmptyMeetingRoleException;
import com.jolupbisang.demo.domain.meeting.exception.EmptyParticipantIdException;
import lombok.Getter;

@Getter
public class ParticipantDetail {
    private Long userId;
    private MeetingRole meetingRole;

    public ParticipantDetail(Long userId, MeetingRole meetingRole) {
        setUserId(userId);
        setRole(meetingRole);
    }

    private void setUserId(Long userId) {
        if (userId == null) {
            throw new EmptyParticipantIdException();
        }
        this.userId = userId;
    }

    private void setRole(MeetingRole meetingRole) {
        if (meetingRole == null) {
            throw new EmptyMeetingRoleException();
        }
        this.meetingRole = meetingRole;
    }
}
