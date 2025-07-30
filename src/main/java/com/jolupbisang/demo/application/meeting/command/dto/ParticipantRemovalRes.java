package com.jolupbisang.demo.application.meeting.command.dto;

public record ParticipantRemovalRes(
        long meetingId,
        long participantId
) {
}
