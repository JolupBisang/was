package com.jolupbisang.demo.application.feedback;

import com.jolupbisang.demo.application.feedback.dto.FeedbackListRes;
import com.jolupbisang.demo.domain.feedback.Feedback;
import com.jolupbisang.demo.infrastructure.feedback.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackDetailQueryService {

    private final FeedbackRepository feedbackRepository;

    public Slice<FeedbackListRes> getFeedbackDetails(long meetingId, long accessUserId, Pageable pageable) {
        Slice<Feedback> feedbacks = feedbackRepository.findByMeetingIdAndUserId(meetingId, accessUserId, pageable);

        return feedbacks.map(FeedbackListRes::from);
    }
}
