package com.jolupbisang.demo.application.segment;

import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.common.MeetingSessionManager;
import com.jolupbisang.demo.application.event.whisper.WhisperDiarizedEvent;
import com.jolupbisang.demo.application.segment.dto.SegmentListRes;
import com.jolupbisang.demo.application.segment.dto.SegmentMessage;
import com.jolupbisang.demo.application.segment.dto.SocketSegmentRes;
import com.jolupbisang.demo.domain.meeting.Meeting;
import com.jolupbisang.demo.domain.segment.Segment;
import com.jolupbisang.demo.domain.user.User;
import com.jolupbisang.demo.global.config.RabbitMQConfig;
import com.jolupbisang.demo.infrastructure.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.audio.client.dto.response.DiarizedResponse;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.segment.SegmentRepository;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final MeetingSessionManager meetingSessionManager;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    private final MeetingAccessValidator meetingAccessValidator;
    private final AudioProgressRepository audioProgressRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional(readOnly = true)
    public Slice<SegmentListRes> getSegments(Long meetingId, Long userId, Pageable pageable) {
        meetingAccessValidator.validateUserParticipating(meetingId, userId);

        Slice<Segment> segments = segmentRepository.findByMeetingId(meetingId, pageable);

        return segments.map(SegmentListRes::from);
    }

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
            if (isCompleted) rabbitTemplate.convertAndSend(
                    RabbitMQConfig.SEGMENT_EXCHANGE,
                    RabbitMQConfig.SEGMENT_ROUTING_KEY,
                    SegmentMessage.of(segmentData, meetingId),
                    message -> {
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    }
            );
            sendCandidateSegments(segmentData, meetingId);
        }
    }

    @Transactional
    public void saveCompletedSegment(SegmentMessage message) {
        long userId = message.segmentData().userId();
        long meetingId = message.meetingId();
        DiarizedResponse.Segment segmentData = message.segmentData();
        LocalDateTime timestamp = calculateTimestamp(segmentData, meetingId);

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
                                        timestamp,
                                        segmentData.text(),
                                        segmentData.lang().isEmpty() ? null : segmentData.lang().get(0))
                        )
                );
    }

    private void sendCandidateSegments(DiarizedResponse.Segment segmentData, long meetingId) {
        meetingSessionManager.sendTextToParticipants(
                SocketResponseType.DIARIZED_SEGMENT,
                meetingId,
                SocketSegmentRes.of(
                        segmentData,
                        calculateTimestamp(segmentData, meetingId)
                )
        );
    }

    private LocalDateTime calculateTimestamp(DiarizedResponse.Segment segmentData, long meetingId) {
        LocalDateTime baseTime = audioProgressRepository.findFirstProcessedTime(meetingId).orElse(null);

        if (baseTime == null || segmentData.words() == null || segmentData.words().isEmpty()) {
            return null;
        }

        double offsetSeconds = segmentData.words().get(0).start() / 16000.0;
        return baseTime.plusNanos((long) (offsetSeconds * 1_000_000_000L));
    }
} 
