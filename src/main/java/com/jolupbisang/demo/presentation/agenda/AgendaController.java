package com.jolupbisang.demo.presentation.agenda;

import com.jolupbisang.demo.application.agenda.service.AgendaService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.agenda.api.AgendaControllerApi;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaCreateReq;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaStatusReq;
import com.jolupbisang.demo.presentation.agenda.dto.request.AgendaUpdateReq;
import com.jolupbisang.demo.presentation.agenda.dto.response.AgendaChangeStatusRes;
import com.jolupbisang.demo.presentation.agenda.dto.response.AgendaCreateRes;
import com.jolupbisang.demo.presentation.agenda.dto.response.AgendaDetailRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AgendaController implements AgendaControllerApi {
    private final AgendaService agendaService;

    @Override
    @PatchMapping("/agendas/status{agendaId}")
    public ResponseEntity<?> changeAgendaStatus(@RequestBody @Valid AgendaStatusReq agendaStatusReq,
                                                @PathVariable("agendaId") Long agendaId,
                                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        AgendaChangeStatusRes response = AgendaChangeStatusRes.of(agendaService.changeAgendaStatus(agendaId, customUserDetails.getUserId(), agendaStatusReq.isCompleted()));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(SuccessResponse.of("회의 안건 상태변경 성공", response));
    }

    @Override
    @GetMapping("/meetings/{meetingId}/agendas")
    public ResponseEntity<?> getAgendas(@PathVariable("meetingId") Long meetingId,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        AgendaDetailRes agendaDetailRes = AgendaDetailRes.fromDto(agendaService.findByMeetingId(meetingId, customUserDetails.getUserId()));

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of(("회의 안건 조회 성공"), agendaDetailRes));
    }

    @PostMapping("/meetings/{meetingId}/agendas")
    public ResponseEntity<?> addAgenda(@PathVariable("meetingId") Long meetingId,
                                       @RequestBody @Valid AgendaCreateReq agendaCreateReq,
                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long agendaId = agendaService.addByMeetingId(meetingId, customUserDetails.getUserId(), agendaCreateReq.content());

        AgendaCreateRes response = AgendaCreateRes.of(agendaId);

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of("회의 안건 추가 성공", response));
    }

    @Override
    @PatchMapping("/agendas/content/{agendaId}")
    public ResponseEntity<?> updateAgenda(@PathVariable("agendaId") Long agendaId,
                                          @RequestBody @Valid AgendaUpdateReq agendaUpdateReq,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        long updatedAgendaId = agendaService.updateByAgendaId(agendaId, customUserDetails.getUserId(), agendaUpdateReq.content());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of("회의 안건 수정 성공", updatedAgendaId));
    }

    @DeleteMapping("/agendas/{agendaId}")
    public ResponseEntity<?> deleteAgenda(@PathVariable("agendaId") Long agendaId,
                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        agendaService.deleteById(agendaId, customUserDetails.getUserId());

        return ResponseEntity.status(HttpStatus.OK).body(SuccessResponse.of("회의 안건 삭제 성공", null));
    }
}
