package com.jolupbisang.demo.infrastructure.meetingUser;

import com.jolupbisang.demo.domain.user.User;

import java.util.List;

public interface MeetingUserRepositoryCustom {
    List<User> findParticipantsByMeetingId(Long meetingId);
}
