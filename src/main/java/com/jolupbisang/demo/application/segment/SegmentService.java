package com.jolupbisang.demo.application.segment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.common.MeetingSessionManager;
import com.jolupbisang.demo.application.event.whisper.WhisperDiarizedEvent;
import com.jolupbisang.demo.application.segment.dto.SocketSegmentRes;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.segment.Segment;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.response.SocketResponse;
import com.jolupbisang.demo.global.response.SocketResponseType;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import com.jolupbisang.demo.infrastructure.segment.SegmentRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final MeetingSessionManager meetingSessionManager;
    private final ObjectMapper objectMapper;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingAccessValidator meetingAccessValidator;

    @EventListener
    public void handleWhisperDiarizedEvent(WhisperDiarizedEvent event) {
        DiarizedResponse diarizedResponse = event.getDiarizedResponse();
        long meetingId = diarizedResponse.groupId();

        processSegment(diarizedResponse.candidate(), true, meetingId);
        processSegment(diarizedResponse.completed(), false, meetingId);
    }

    private void processSegment(List<DiarizedResponse.Segment> segments, boolean isCompleted, long meetingId) {
        if (segments == null || segments.isEmpty()) {
            return;
        }

        for (DiarizedResponse.Segment segmentData : segments) {
            if (segmentData.userId() != segmentData.audioUserId()) {
                log.warn("[SegmentService] User ID ({}) and Audio User ID ({}) do not match. Skipping segment for meetingId: {}.", segmentData.userId(), segmentData.audioUserId(), meetingId);
                continue;
            }

            if (isCompleted) saveCompletedSegment(segmentData, meetingId);
            else sendCandidateSegments(segmentData, meetingId);
        }
    }

    private void saveCompletedSegment(DiarizedResponse.Segment segmentData, long meetingId) {
        long userId = segmentData.userId();

        try {
            meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);
        } catch (Exception e) {
            log.warn("[SegmentService] Validation failed for meetingId: {}, userId: {}. Error: {}", meetingId, userId, e.getMessage());
            return;
        }

        Meeting meeting = meetingRepository.findById(meetingId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (meeting == null || user == null) {
            log.warn("[SegmentService] Meeting ({}) or User ({}) not found. Skipping segment.", meetingId, userId);
            return;
        }

        segmentRepository.findByMeetingIdAndSegmentOrder(meetingId, segmentData.order())
                .ifPresentOrElse(
                        existingSegment ->
                                existingSegment.updateDetails(
                                        segmentData.text(),
                                        segmentData.lang().isEmpty() ? null : segmentData.lang().get(0),
                                        user
                                ),
                        () -> segmentRepository.save(
                                new Segment(
                                        meeting,
                                        user,
                                        segmentData.order(),
                                        segmentData.text(),
                                        segmentData.lang().isEmpty() ? null : segmentData.lang().get(0))
                        )
                );
    }

    private void sendCandidateSegments(DiarizedResponse.Segment segmentData, long meetingId) {
        List<WebSocketSession> sessionsInMeeting = meetingSessionManager.findAllByMeetingId(meetingId);
        if (sessionsInMeeting.isEmpty()) {
            log.warn("[SegmentService] No active WebSocket sessions found for meetingId: {}. Cannot send candidate segments.", meetingId);
            return;
        }

        SocketSegmentRes socketSegmentRes = SocketSegmentRes.of(segmentData, LocalDateTime.now());
        SocketResponse<SocketSegmentRes> socketResponse = SocketResponse.of(SocketResponseType.DIARIZED_SEGMENT, socketSegmentRes);

        try {
            String messagePayload = objectMapper.writeValueAsString(socketResponse);
            TextMessage textMessage = new TextMessage(messagePayload);

            for (WebSocketSession session : sessionsInMeeting) {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            }
        } catch (IOException e) {
            log.error("[SegmentService] Error sending candidate segment (order: {}) via WebSocket for meetingId: {}: {}", segmentData.order(), meetingId, e.getMessage());
        }
    }
} 
