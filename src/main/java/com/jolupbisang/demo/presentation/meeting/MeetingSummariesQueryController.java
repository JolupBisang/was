package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.MeetingSummariesQueryService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingSummariesQueryController {

    private final MeetingSummariesQueryService meetingSummariesQueryService;

    @GetMapping("/api/v1/meetings")
    public ResponseEntity<?> getMeetingSummaries(@RequestParam("year") Integer year,
                                                 @RequestParam("month") Integer month,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(meetingSummariesQueryService.getMeetingDetailSummary(year, month, userDetails.getUserId()));
    }
}
