package com.jolupbisang.demo.application.feedback.query;

import com.jolupbisang.demo.application.feedback.query.dto.FeedbackListRes;
import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.domain.feedback.Feedback;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.BusinessException;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.feedback.FeedbackRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackDetailQueryService {

    private final FeedbackRepository feedbackRepository;
    private final MeetingRepository meetingRepository;

    public Slice<FeedbackListRes> getFeedbackDetails(long meetingId, long accessUserId, Pageable pageable) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        if (!meeting.isParticipant(accessUserId)) {
            throw new BusinessException(MeetingApplicationErrorCode.NOT_PARTICIPANT, "userId: %d", accessUserId);
        }

        Slice<Feedback> feedbacks = feedbackRepository.findByMeetingIdAndUserId(meetingId, accessUserId, pageable);

        return feedbacks.map(FeedbackListRes::from);
    }
}
