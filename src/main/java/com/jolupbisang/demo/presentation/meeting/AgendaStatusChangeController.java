package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.AgendaStatusChangeService;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaStatusChangeRes;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaStatusReq;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgendaStatusChangeController {

    private final AgendaStatusChangeService agendaStatusChangeService;

    @PatchMapping("/api/v1/meetings/{meetingId}/agendas/{agendaId}/status")
    public ResponseEntity<AgendaStatusChangeRes> changeAgendaStatus(@RequestBody @Valid AgendaStatusReq agendaStatusReq,
                                                                    @PathVariable("meetingId") Long meetingId,
                                                                    @PathVariable("agendaId") Long agendaId,
                                                                    @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(agendaStatusChangeService.changeAgendaStatus(meetingId, agendaId, customUserDetails.getUserId(), agendaStatusReq.isCompleted()));
    }
}
