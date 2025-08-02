package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.AgendaDeletionService;
import com.jolupbisang.demo.application.meeting.command.dto.AgendaDeletionRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgendaDeletionController {
    private final AgendaDeletionService agendaDeletionService;

    @DeleteMapping("/api/v1/meetings/{meetingId}/agendas/{agendaId}")
    public ResponseEntity<AgendaDeletionRes> deleteAgenda(@PathVariable("meetingId") Long meetingId,
                                                          @PathVariable("agendaId") Long agendaId,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(agendaDeletionService.delete(meetingId, agendaId, customUserDetails.getUserId()));
    }
} 
