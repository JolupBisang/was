package com.jolupbisang.demo.presentation.team;

import com.jolupbisang.demo.application.team.command.TeamDeletionService;
import com.jolupbisang.demo.application.team.command.dto.TeamDeletionRes;
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
public class TeamDeletionController {

    private final TeamDeletionService teamDeletionService;

    @DeleteMapping("/api/v1/teams/{teamId}")
    public ResponseEntity<TeamDeletionRes> deleteTeam(
            @PathVariable("teamId") Long teamId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        TeamDeletionRes res = teamDeletionService.delete(teamId, userDetails.getUserId());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(res);
    }
}

