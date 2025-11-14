package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.MeetingSearchQueryService;
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
public class MeetingSearchQueryController {

    private final MeetingSearchQueryService meetingSearchQueryService;

    @GetMapping("/api/v1/meetings/search")
    public ResponseEntity<Slice<MeetingDetailSummary>> searchMeetings(
            @RequestParam String title,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable) {
        Slice<MeetingDetailSummary> summaries = meetingSearchQueryService.searchMeetingsByTitle(title, userDetails.getUserId(), pageable);
        return ResponseEntity.ok(summaries);
    }
}

