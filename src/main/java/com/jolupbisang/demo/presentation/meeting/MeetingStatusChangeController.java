package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.MeetingStatusChangeService;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingStatusChangeRes;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingStatusUpdateReq;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingStatusChangeController {

    private final MeetingStatusChangeService meetingStatusChangeService;

    @PutMapping("/api/v1/meetings/{meetingId}/status")
    public ResponseEntity<MeetingStatusChangeRes> updateMeetingStatusV1(@PathVariable Long meetingId,
                                                                        @Valid @RequestBody MeetingStatusUpdateReq meetingStatusUpdateReq,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(meetingStatusChangeService.changeMeetingStatus(meetingId, userDetails.getUserId(), meetingStatusUpdateReq));
    }
}
