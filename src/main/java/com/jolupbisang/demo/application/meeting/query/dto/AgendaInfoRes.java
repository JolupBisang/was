package com.jolupbisang.demo.application.meeting.query.dto;

public record AgendaInfoRes(
        long agendaId,
        String content,
        boolean isCompleted
) {
}
