package com.jolupbisang.demo.presentation.team;

import com.jolupbisang.demo.application.team.query.TeamDetailQueryService;
import com.jolupbisang.demo.application.team.query.dto.TeamDetailRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamDetailQueryController {

    private final TeamDetailQueryService teamDetailQueryService;

    @GetMapping("/api/v1/teams/{teamId}")
    public ResponseEntity<TeamDetailRes> getTeamDetail(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        TeamDetailRes teamDetail = teamDetailQueryService.getTeamDetail(teamId, userDetails.getUserId());
        
        return ResponseEntity.ok(teamDetail);
    }
}

