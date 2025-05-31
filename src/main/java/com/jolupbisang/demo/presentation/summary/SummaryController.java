package com.jolupbisang.demo.presentation.summary;

import com.jolupbisang.demo.application.summary.SummaryService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.summary.api.SummaryControllerApi;
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
@RequestMapping("/api/summary")
public class SummaryController implements SummaryControllerApi {

    private final SummaryService summaryService;

    @GetMapping(path = "/subscribe/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long meetingId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return summaryService.subscribe(meetingId, userDetails.getUserId());
    }
}
