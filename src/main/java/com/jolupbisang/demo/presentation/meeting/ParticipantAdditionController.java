package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.ParticipantAdditionService;
import com.jolupbisang.demo.application.meeting.command.dto.ParticipantAdditionRes;
import com.jolupbisang.demo.domain.meeting.dto.ParticipantAddReq;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParticipantAdditionController {

    private final ParticipantAdditionService participantAdditionService;

    @PostMapping("/api/v1/meeting/{meetingId}/participants")
    public ResponseEntity<ParticipantAdditionRes> addParticipant(@PathVariable long meetingId,
                                                                 @RequestBody @Valid ParticipantAddReq participantAddReq,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(participantAdditionService.addParticipants(meetingId, userDetails.getUserId(), participantAddReq));
    }
}
