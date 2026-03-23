package com.jolupbisang.demo.application.meeting.query.dto;

import com.jolupbisang.demo.domain.meeting.model.MeetingRole;

public record ParticipantInfoRes(
        Long userId,
        String email,
        MeetingRole role
) {
}
