package com.jolupbisang.demo.presentation.agenda.dto.response;

public record AgendaCreateRes(
        long agendaId
) {
    public static AgendaCreateRes of(long agendaId) {
        return new AgendaCreateRes(agendaId);
    }
} 