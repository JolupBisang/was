package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.dto.MeetingReq;
import com.jolupbisang.demo.application.meeting.service.MeetingService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<?> createMeeting(@Valid @RequestBody MeetingReq meetingReq,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        meetingService.createMeeting(meetingReq, userDetails.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body("회의 생성 성공");
    }
}
