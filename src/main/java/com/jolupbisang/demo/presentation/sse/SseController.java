package com.jolupbisang.demo.presentation.sse;

import com.jolupbisang.demo.application.feedback.FeedbackService;
import com.jolupbisang.demo.application.participationRate.ParticipationRateService;
import com.jolupbisang.demo.application.summary.SummaryService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.sse.api.SseControllerApi;
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
@RequestMapping("/api/sse/subscribe")
public class SseController implements SseControllerApi {

    private final SummaryService summaryService;
    private final ParticipationRateService participationRateService;
    private final FeedbackService feedbackService;

    @Override
    @GetMapping(path = "/summary/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeSummary(@PathVariable Long meetingId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        return summaryService.subscribe(meetingId, userDetails.getUserId());
    }


    @Override
    @GetMapping(path = "/participation-rate/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeParticipationRate(@PathVariable(value = "meetingId") Long meetingId,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        return participationRateService.subscribe(meetingId, userDetails.getUserId());
    }


    @Override
    @GetMapping(value = "/feedback/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeFeedback(@PathVariable Long meetingId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {

        return feedbackService.subscribe(meetingId, userDetails.getUserId());
    }
}
