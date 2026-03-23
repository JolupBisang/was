package com.jolupbisang.demo.presentation.team;

import com.jolupbisang.demo.application.team.command.TeamMemberAdditionService;
import com.jolupbisang.demo.application.team.command.dto.TeamMemberAdditionReq;
import com.jolupbisang.demo.application.team.command.dto.TeamMemberAdditionRes;
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
public class TeamMemberAdditionController {

    private final TeamMemberAdditionService teamMemberAdditionService;

    @PostMapping("/api/v1/teams/{teamId}/members")
    public ResponseEntity<TeamMemberAdditionRes> addMember(
            @PathVariable Long teamId,
            @RequestBody @Valid TeamMemberAdditionReq request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teamMemberAdditionService.addMember(teamId, userDetails.getUserId(), request));
    }
}

