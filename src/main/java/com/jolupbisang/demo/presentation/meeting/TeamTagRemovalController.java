package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.TeamTagRemovalService;
import com.jolupbisang.demo.application.meeting.command.dto.TeamTagRemovalReq;
import com.jolupbisang.demo.application.meeting.command.dto.TeamTagRemovalRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamTagRemovalController {

    private final TeamTagRemovalService teamTagRemovalService;

    @DeleteMapping("/api/v1/meetings/{meetingId}/team-tags")
    public ResponseEntity<TeamTagRemovalRes> removeTeamTag(
            @PathVariable Long meetingId,
            @RequestBody @Valid TeamTagRemovalReq request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(teamTagRemovalService.removeTeamTag(meetingId, userDetails.getUserId(), request));
    }
}

