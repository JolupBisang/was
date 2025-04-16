package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.dto.MeetingReq;
import com.jolupbisang.demo.application.meeting.service.MeetingService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.meeting.dto.response.MeetingCreationRes;
import com.jolupbisang.demo.presentation.meeting.dto.response.MeetingDetailRes;
import com.jolupbisang.demo.presentation.meeting.dto.response.MeetingDetailSummaryRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<?> createMeeting(@Valid @RequestBody MeetingReq meetingReq,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        MeetingCreationRes response = MeetingCreationRes.of(meetingService.createMeeting(meetingReq, userDetails.getUserId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of("회의 생성 성공", response));
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<?> getMeetingDetail(@PathVariable Long meetingId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        MeetingDetailRes meetingDetail = meetingService.getMeetingDetail(meetingId, userDetails.getUserId());

        return ResponseEntity.ok(SuccessResponse.of("회의 조회 성공", meetingDetail));
    }

    @GetMapping
    public ResponseEntity<?> getMeetings(@RequestParam("year") Integer year,
                                         @RequestParam("month") Integer month,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        MeetingDetailSummaryRes response = MeetingDetailSummaryRes.fromDto(
                meetingService.getMeetingsByYearAndMonth(year, month, userDetails.getUserId())
        );

        return ResponseEntity.ok(SuccessResponse.of("회의 목록 조회 성공", response));
    }
}
