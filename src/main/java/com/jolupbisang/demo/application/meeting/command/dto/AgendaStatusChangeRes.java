package com.jolupbisang.demo.application.meeting.command.dto;

public record AgendaStatusChangeRes(
        long meetingId,
        long agendaId,
        boolean isCompleted
) {
}
