package com.jolupbisang.demo.domain.meeting.event;

public record AgendaStatusChangedEvent(
        Long agendaId,
        boolean isCompleted
) {
}
