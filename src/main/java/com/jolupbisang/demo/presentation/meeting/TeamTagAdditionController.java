package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.command.TeamTagAdditionService;
import com.jolupbisang.demo.application.meeting.command.dto.TeamTagAdditionReq;
import com.jolupbisang.demo.application.meeting.command.dto.TeamTagAdditionRes;
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
public class TeamTagAdditionController {

    private final TeamTagAdditionService teamTagAdditionService;

    @PostMapping("/api/v1/meetings/{meetingId}/team-tags")
    public ResponseEntity<TeamTagAdditionRes> addTeamTag(
            @PathVariable Long meetingId,
            @RequestBody @Valid TeamTagAdditionReq request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teamTagAdditionService.addTeamTag(meetingId, userDetails.getUserId(), request));
    }
}

