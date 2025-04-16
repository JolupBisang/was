package com.jolupbisang.demo.presentation.agenda;

import com.jolupbisang.demo.application.agenda.service.AgendaService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.agenda.api.AgendaControllerApi;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaStatusReq;
import com.jolupbisang.demo.presentation.agenda.dto.response.AgendaChangeStatusRes;
import com.jolupbisang.demo.presentation.agenda.dto.response.AgendaDetailRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agendas")
public class AgendaController implements AgendaControllerApi {
    private final AgendaService agendaService;

    @Override
    @PatchMapping("/{agendaId}")
    public ResponseEntity<?> changeAgendaStatus(@RequestBody @Valid AgendaStatusReq agendaStatusReq,
                                                @PathVariable("agendaId") Long agendaId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        AgendaChangeStatusRes response = AgendaChangeStatusRes.of(agendaService.changeAgendaStatus(agendaId, customUserDetails.getUserId(), agendaStatusReq.isCompleted()));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.of("회의 안건 상태변경 성공", response));
    }

    @Override
    @GetMapping("/{meetingId}")
    public ResponseEntity<?> getAgendas(@PathVariable("meetingId") Long meetingId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        AgendaDetailRes agendaDetailRes = AgendaDetailRes.fromDto(agendaService.findByMeetingId(meetingId, customUserDetails.getUserId()));

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(("회의 안건 조회 성공"), agendaDetailRes));
    }
}
