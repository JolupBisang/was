package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.ParticipantRemovalService;
import com.jolupbisang.demo.application.meeting.command.dto.ParticipantRemovalRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipantRemovalController {

    private final ParticipantRemovalService participantRemovalService;

    @DeleteMapping("/api/v1/meetings/{meetingId}/participant/{participantId}")
    public ResponseEntity<ParticipantRemovalRes> removeParticipant(
            @PathVariable Long meetingId,
            @PathVariable Long participantId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(participantRemovalService.removeParticipant(meetingId, customUserDetails.getUserId(), participantId));
    }
}
