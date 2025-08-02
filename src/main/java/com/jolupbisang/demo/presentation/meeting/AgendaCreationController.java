package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.AgendaCreationService;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaCreateReq;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaCreationRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgendaCreationController {

    private final AgendaCreationService agendaCreationService;

    @PostMapping("/api/v1/{meetingId}/agendas")
    public ResponseEntity<AgendaCreationRes> addAgenda(@PathVariable("meetingId") Long meetingId,
                                                       @RequestBody @Valid AgendaCreateReq agendaCreateReq,
                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(agendaCreationService.create(meetingId, customUserDetails.getUserId(), agendaCreateReq));
    }
} 
