package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.AgendaUpdateService;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaUpdateReq;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaUpdateRes;
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
public class AgendaUpdateController {

    private final AgendaUpdateService agendaUpdateService;

    @PatchMapping("/api/v1/meetings/{meetingId}/agendas/{agendaId}")
    public ResponseEntity<AgendaUpdateRes> updateContent(@PathVariable("meetingId") Long meetingId,
                                                         @PathVariable("agendaId") Long agendaId,
                                                         @RequestBody @Valid AgendaUpdateReq agendaUpdateReq,
                                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendaUpdateService.updateContent(meetingId, agendaId, customUserDetails.getUserId(), agendaUpdateReq));
    }
} 
