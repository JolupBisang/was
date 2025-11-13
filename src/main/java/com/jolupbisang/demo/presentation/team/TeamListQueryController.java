package com.jolupbisang.demo.presentation.team;

import com.jolupbisang.demo.application.team.query.TeamListQueryService;
import com.jolupbisang.demo.application.team.query.dto.TeamListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TeamListQueryController {

    private final TeamListQueryService teamListQueryService;

    @GetMapping("/api/v1/teams")
    public ResponseEntity<TeamListRes> getMyTeams(@AuthenticationPrincipal CustomUserDetails userDetails) {
        TeamListRes teams = teamListQueryService.getTeamsByUserId(userDetails.getUserId());
        
        return ResponseEntity.ok(teams);
    }
}

