package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.MeetingEventSubscribeService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class MeetingEventSubscribeController {

    private final MeetingEventSubscribeService meetingEventSubscribeService;

    @GetMapping(path = "/api/v1/meetings/{meetingId}/events/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long meetingId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return meetingEventSubscribeService.subscribe(meetingId, userDetails.getUserId());
    }
}
