package com.jolupbisang.demo.presentation.team;

import com.jolupbisang.demo.application.team.TeamCreationService;
import com.jolupbisang.demo.application.team.command.dto.TeamCreationReq;
import com.jolupbisang.demo.application.team.command.dto.TeamCreationRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamCreationController {

    private final TeamCreationService teamCreationService;

    @PostMapping("/api/v1/teams")
    public ResponseEntity<TeamCreationRes> createTeam(@RequestBody @Valid TeamCreationReq teamCreationReq,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teamCreationService.create(teamCreationReq, userDetails.getUserId()));
    }
}
