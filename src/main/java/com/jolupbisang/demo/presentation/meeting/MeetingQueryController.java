package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.MeetingQueryService;
import com.jolupbisang.demo.application.meeting.query.dto.MeetingDetailSummary;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingQueryController {

    private final MeetingQueryService meetingQueryService;

    @GetMapping("/api/v1/meetings")
    public ResponseEntity<Slice<MeetingDetailSummary>> getMeetings(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String title,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        Slice<MeetingDetailSummary> meetings = meetingQueryService.getMeetings(
            userDetails.getUserId(), year, month, title, pageable
        );
        return ResponseEntity.ok(meetings);
    }
}

