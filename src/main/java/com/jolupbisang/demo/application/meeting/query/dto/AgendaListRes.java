package com.jolupbisang.demo.application.meeting.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Agenda;

import java.util.List;

public record AgendaListRes(
        List<AgendaListInfoRes> agendas
) {

    public static AgendaListRes from(List<Agenda> agendas) {
        return new AgendaListRes(agendas.stream()
                .map(a -> new AgendaListInfoRes(a.getId(), a.getContent(), a.getIsCompleted()))
                .toList());
    }

    private record AgendaListInfoRes(
            long id,
            String content,
            boolean isCompleted
    ) {
    }
}
