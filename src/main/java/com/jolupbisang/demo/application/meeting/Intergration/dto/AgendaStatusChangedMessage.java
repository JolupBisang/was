package com.jolupbisang.demo.application.meeting.Intergration.dto;

public record AgendaStatusChangedMessage(
        long agendaId,
        boolean isCompleted
) {

    public static AgendaStatusChangedMessage of(long agendaId, boolean isCompleted) {
        return new AgendaStatusChangedMessage(agendaId, isCompleted);
    }
}
