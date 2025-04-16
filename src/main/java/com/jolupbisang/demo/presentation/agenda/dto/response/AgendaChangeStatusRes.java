package com.jolupbisang.demo.presentation.agenda.dto.response;

public record AgendaChangeStatusRes(
        boolean isCompleted
) {

    public static AgendaChangeStatusRes of(boolean isCompleted) {
        return new AgendaChangeStatusRes(isCompleted);
    }
}
