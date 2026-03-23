package com.jolupbisang.demo.domain.meeting.event;

public record AgendaStatusChangedEvent(
        long meetingId,
        long agendaId,
        boolean isCompleted
) {
}
