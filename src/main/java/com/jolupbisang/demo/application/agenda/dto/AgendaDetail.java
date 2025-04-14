package com.jolupbisang.demo.application.agenda.dto;

import com.jolupbisang.demo.domain.agenda.Agenda;

public record AgendaDetail(
        Long agendaId,
        String content,
        boolean isCompleted
) {

    public static AgendaDetail fromEntity(Agenda agenda) {
        return new AgendaDetail(
                agenda.getId(),
                agenda.getContent(),
                agenda.isCompleted()
        );
    }
}
