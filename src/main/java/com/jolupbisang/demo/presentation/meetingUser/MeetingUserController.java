package com.jolupbisang.demo.presentation.meetingUser;

import com.jolupbisang.demo.application.meetingUser.service.MeetingUserService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.meetingUser.api.MeetingUserControllerApi;
import com.jolupbisang.demo.presentation.meetingUser.dto.request.ParticipantAddReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meeting-users")
public class MeetingUserController implements MeetingUserControllerApi {

    private final MeetingUserService meetingUserService;

    @Override
    @PostMapping("/{meetingId}")
    public ResponseEntity<Void> addMeetingUser(
            @PathVariable Long meetingId,
            @RequestBody @Valid ParticipantAddReq request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        meetingUserService.addParticipants(meetingId, customUserDetails.getUserId(), request.emails());
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{meetingId}/{participantUserId}")
    public ResponseEntity<Void> removeMeetingUser(
            @PathVariable Long meetingId,
            @PathVariable Long participantUserId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        meetingUserService.removeParticipant(meetingId, customUserDetails.getUserId(), participantUserId);
        return ResponseEntity.noContent().build();
    }
} 
