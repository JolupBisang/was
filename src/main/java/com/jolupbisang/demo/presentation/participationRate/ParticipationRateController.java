package com.jolupbisang.demo.presentation.participationRate;

import com.jolupbisang.demo.application.participationRate.ParticipationRateService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meeting/participation_rate")
public class ParticipationRateController {

    private final ParticipationRateService participationRateService;

    @GetMapping(path = "/subscribe/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable(value = "meetingId") Long meetingId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return participationRateService.subscribe(meetingId, userDetails.getUserId());
    }
}
