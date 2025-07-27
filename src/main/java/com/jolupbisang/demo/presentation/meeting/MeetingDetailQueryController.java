package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.MeetingDetailQueryService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingDetailQueryController {

    private final MeetingDetailQueryService meetingDetailQueryService;

    @GetMapping("/api/v1/meeting/{meetingId}")
    public ResponseEntity<?> getMeetingDetail(@PathVariable Long meetingId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(meetingDetailQueryService.getMeetingDetail(meetingId, userDetails.getUserId()));
    }

}
