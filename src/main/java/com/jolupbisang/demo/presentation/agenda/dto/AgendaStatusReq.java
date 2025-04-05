package com.jolupbisang.demo.presentation.agenda.dto;

import jakarta.validation.constraints.NotNull;

public record AgendaStatusReq(
        @NotNull(message = "완료 상태는 null일 수 없습니다.")
        Boolean isCompleted
) {
}
