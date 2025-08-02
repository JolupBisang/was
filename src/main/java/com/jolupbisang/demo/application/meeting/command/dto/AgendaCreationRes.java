package com.jolupbisang.demo.application.meeting.command.dto;

import com.jolupbisang.demo.domain.meeting.dto.CreatedAgendaDetail;

import java.util.List;

public record AgendaCreationRes(
        long meetingId,
        List<AgendaDetailRes> agendaDetails
) {

    public static AgendaCreationRes of(long meetingId, List<CreatedAgendaDetail> agendaDetails) {
        return new AgendaCreationRes(
                meetingId,
                agendaDetails.stream()
                        .map(detail -> new AgendaDetailRes(detail.id(), detail.content()))
                        .toList()
        );
    }
}

record AgendaDetailRes(
        long agendaId,
        String content
) {
}
