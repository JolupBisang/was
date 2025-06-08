package com.jolupbisang.demo.presentation.participationRate;

import com.jolupbisang.demo.application.participationRate.ParticipationRateService;
import com.jolupbisang.demo.application.participationRate.dto.ParticipationRateHistoryRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.participationRate.api.ParticipationRateControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participation_rate")
public class ParticipationRateController implements ParticipationRateControllerApi {

    private final ParticipationRateService participationRateService;

    @Override
    @GetMapping("/{meetingId}")
    public ParticipationRateHistoryRes getParticipationRateHistory(@PathVariable Long meetingId,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return participationRateService.getParticipationRateByMeetingId(meetingId, userDetails.getUserId());
    }
}
