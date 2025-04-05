package com.jolupbisang.demo.presentation.agenda.dto;

import jakarta.validation.constraints.NotNull;

public record AgendaStatusRequest(
        @NotNull(message = "isCompleted는 null일 수 없습니다.")
        boolean isCompleted
) {
}
