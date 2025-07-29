package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.MeetingDetailUpdateService;
import com.jolupbisang.demo.application.meeting.command.dto.MeetingDetailUpdateRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingUpdateReq;
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
public class MeetingDetailUpdateController {

    private final MeetingDetailUpdateService meetingDetailUpdateService;

    @PutMapping("/api/v1/meetings/{meetingId)")
    public ResponseEntity<MeetingDetailUpdateRes> updateMeetingDetail(@PathVariable Long meetingId,
                                                                      @Valid @RequestBody MeetingUpdateReq meetingUpdateReq,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(meetingDetailUpdateService.updateDetail(meetingId, userDetails.getUserId(), meetingUpdateReq));
    }
}
