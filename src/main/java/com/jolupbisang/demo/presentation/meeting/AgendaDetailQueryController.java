package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.AgendaDetailQueryService;
import com.jolupbisang.demo.application.meeting.query.dto.AgendaListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgendaDetailQueryController {

    private final AgendaDetailQueryService agendaDetailQueryService;

    @GetMapping("/api/v1/meetings/{meetingId}/agendas")
    public ResponseEntity<AgendaListRes> getAgendas(@PathVariable Long meetingId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        AgendaListRes agendaDetailRes = agendaDetailQueryService.getAgendas(meetingId, userDetails.getUserId());

        return ResponseEntity.ok(agendaDetailRes);
    }
}
