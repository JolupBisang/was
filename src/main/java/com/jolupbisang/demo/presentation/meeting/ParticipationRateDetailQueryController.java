package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.ParticipationRateDetailQueryService;
import com.jolupbisang.demo.application.meeting.query.dto.ParticipationRateRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipationRateDetailQueryController {

    private final ParticipationRateDetailQueryService participationRateDetailQueryService;

    @GetMapping("/api/v1/meetings/{meetingId}/participation-rate")
    public ResponseEntity<ParticipationRateRes> getParticipationRateByMeetingId(@PathVariable Long meetingId,
                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(participationRateDetailQueryService.getParticipationRateByMeeting(meetingId, userDetails.getUserId()));
    }
}
