package com.jolupbisang.demo.application.feedback;

import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.event.FeedbackReceivedEvent;
import com.jolupbisang.demo.application.feedback.dto.SseFeedbackRes;
import com.jolupbisang.demo.application.feedback.exception.FeedbackErrorCode;
import com.jolupbisang.demo.domain.feedback.Feedback;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.exception.CustomException;
import com.jolupbisang.demo.infrastructure.feedback.FeedbackRepository;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseService;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    private final MeetingAccessValidator meetingAccessValidator;
    private final MeetingSseService meetingSseService;

    public SseEmitter subscribe(Long meetingId, Long userId) {
        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);

        return meetingSseService.subscribe(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.FEEDBACK);
    }

    @EventListener
    @Transactional
    public void handleFeedbackReceived(FeedbackReceivedEvent event) {
        long meetingId = event.getMeetingId();
        long userId = event.getUserId();
        String comment = event.getComment();
        LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault());

        Meeting meeting;
        User user;
        try {
            meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);
            meeting = meetingRepository.findById(meetingId)
                    .orElseThrow(() -> new CustomException(FeedbackErrorCode.MEETING_NOT_FOUND));
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(FeedbackErrorCode.USER_NOT_FOUND));
        } catch (CustomException e) {
            log.error("[Whisper Feedback Error] {}", e.getErrorCode().getMessage(), e);
            return;
        }

        feedbackRepository.save(new Feedback(meeting, user, comment, timestamp));
        meetingSseService.sendEventToUserOfMeeting(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.FEEDBACK, SseFeedbackRes.of(timestamp, comment));
    }
}
