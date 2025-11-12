package com.jolupbisang.demo.presentation.team;

import com.jolupbisang.demo.application.team.query.TeamMemberQueryService;
import com.jolupbisang.demo.application.team.query.dto.TeamMemberRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamMemberQueryController {

    private final TeamMemberQueryService teamMemberQueryService;

    @GetMapping("/api/v1/teams/{teamId}/members")
    public ResponseEntity<TeamMemberRes> getTeamMembers(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TeamMemberRes members = teamMemberQueryService.getTeamMembers(teamId, userDetails.getUserId());

        return ResponseEntity.ok(members);
    }
}

