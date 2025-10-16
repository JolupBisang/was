package com.jolupbisang.demo.presentation.feedback;

import com.jolupbisang.demo.application.feedback.query.FeedbackDetailQueryService;
import com.jolupbisang.demo.application.feedback.query.dto.FeedbackListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FeedbackQueryController {

    private final FeedbackDetailQueryService feedbackService;

    @GetMapping("/api/v1/meetings/{meetingId}/feedbacks")
    public Slice<FeedbackListRes> getFeedbacks(@PathVariable long meetingId,
                                               @PageableDefault(size = 30, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        return feedbackService.getFeedbackDetails(meetingId, userDetails.getUserId(), pageable);
    }
}
