package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.MeetingCreationService;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingCreationReq;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingCreationRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingCreationController {

    private final MeetingCreationService meetingCreationService;

    @PostMapping("/api/v1/meetings")
    public ResponseEntity<MeetingCreationRes> createMeeting(@Valid @RequestBody MeetingCreationReq meetingCreationReq,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(meetingCreationService.create(meetingCreationReq, userDetails.getUserId()));
    }
}
