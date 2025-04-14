package com.jolupbisang.demo.presentation.agenda.dto;

import com.jolupbisang.demo.application.agenda.dto.AgendaDetail;

import java.util.List;

public record AgendaDetailRes(
        List<DetailDto> agendaDetails
) {
    public static AgendaDetailRes fromDto(List<AgendaDetail> details) {
        return new AgendaDetailRes(
                details.stream()
                        .map(detail ->
                                new DetailDto(detail.agendaId(), detail.content(), detail.isCompleted()))
                        .toList());
    }

    private record DetailDto(
            Long agendaId,
            String content,
            boolean isCompleted
    ) {
    }
}
