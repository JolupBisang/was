package com.jolupbisang.demo.presentation.agenda;

import com.jolupbisang.demo.application.agenda.service.AgendaService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.agenda.dto.AgendaStatusRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agendas")
public class AgendaController {
    private final AgendaService agendaService;

    @PatchMapping("/{agendaId}")
    public ResponseEntity<?> changeAgendaStatus(@RequestBody @Valid AgendaStatusRequest agendaStatusRequest,
                                                @PathVariable("agendaId") Long agendaId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        agendaService.changeAgendaStatus(
                agendaId,
                customUserDetails.getUserId(),
                agendaStatusRequest.isCompleted());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("회의 안건 상태 변경 성공");
    }
}
