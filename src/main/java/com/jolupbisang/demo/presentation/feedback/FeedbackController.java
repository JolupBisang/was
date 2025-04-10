package com.jolupbisang.demo.presentation.feedback;

import com.jolupbisang.demo.application.feedback.FeedbackService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping(value = "/subscribe/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long meetingId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return feedbackService.subscribe(meetingId, userDetails.getUserId());
    }

    @PostMapping("/send/{meetingId}")
    public void sendTestFeedback(@PathVariable Long meetingId) {
        feedbackService.sendFeedback(meetingId);
    }
}
