package com.jolupbisang.demo.presentation.feedback;

import com.jolupbisang.demo.application.feedback.FeedbackService;
import com.jolupbisang.demo.application.feedback.dto.FeedbackListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.feedback.api.FeedbackControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackController implements FeedbackControllerApi {

    private final FeedbackService feedbackService;

    @GetMapping(value = "/subscribe/{meetingId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long meetingId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {

        return feedbackService.subscribe(meetingId, userDetails.getUserId());
    }

    @GetMapping("/{meetingId}")
    public Slice<FeedbackListRes> getFeedbacks(@PathVariable Long meetingId,
                                             @PageableDefault(size = 30, sort = "timeStamp", direction = Sort.Direction.DESC) Pageable pageable,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        return feedbackService.getFeedbacks(meetingId, userDetails.getUserId(), pageable);
    }
}
