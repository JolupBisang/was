package com.jolupbisang.demo.presentation.participationRate;

import com.jolupbisang.demo.application.participationRate.ParticipationRateService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.participationRate.api.ParticipationRateControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participation_rate")
public class ParticipationRateController implements ParticipationRateControllerApi {

    private final ParticipationRateService participationRateService;

    @GetMapping(path = "/subscribe/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable(value = "meetingId") Long meetingId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return participationRateService.subscribe(meetingId, userDetails.getUserId());
    }

    @PostMapping("/send/{meetingId}")
    public void sendTestSummary(@PathVariable Long meetingId) {
        participationRateService.sendTestMessage(meetingId);
    }
}
