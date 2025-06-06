package com.jolupbisang.demo.application.participationRate;

import com.jolupbisang.demo.application.common.MeetingAccessValidator;
import com.jolupbisang.demo.application.event.MeetingCompletedEvent;
import com.jolupbisang.demo.application.event.MeetingStartingEvent;
import com.jolupbisang.demo.application.event.whisper.WhisperDiarizedEvent;
import com.jolupbisang.demo.application.participationRate.dto.ParticipationRateRes;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRateService {

    private final MeetingSseService meetingSseService;
    private final MeetingAccessValidator meetingAccessValidator;
    private final TaskScheduler taskScheduler;

    private final Map<Long, Map<Long, Long>> meetingParticipationData = new ConcurrentHashMap<>();
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private static final int PARTICIPATION_RATE_SEND_INTERVAL_SECONDS = 30;

    public SseEmitter subscribe(Long meetingId, Long userId) {
        meetingAccessValidator.validateMeetingInProgressAndUserParticipating(meetingId, userId);

        return meetingSseService.subscribe(String.valueOf(meetingId), String.valueOf(userId), MeetingSseEventType.PARTICIPATION_RATE);
    }
    
    @EventListener
    public void addToParticipationData(WhisperDiarizedEvent event) {
        DiarizedResponse diarizedResponse = event.getDiarizedResponse();
        long groupId = diarizedResponse.groupId();

        for (DiarizedResponse.Segment segment : diarizedResponse.completed()) {
            if (segment.userId() == segment.audioUserId()) {
                processSegment(groupId, segment);
            }
        }
    }

    @Async("AsyncTaskExecutor")
    @EventListener
    public void handleMeetingStart(MeetingStartingEvent event) {
        long meetingId = event.getMeetingId();

        Runnable task = () -> sendParticipationRateUpdate(meetingId);

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
                task,
                Instant.now().plusSeconds(PARTICIPATION_RATE_SEND_INTERVAL_SECONDS * 2),
                Duration.ofSeconds(PARTICIPATION_RATE_SEND_INTERVAL_SECONDS));

        scheduledTasks.put(meetingId, scheduledFuture);
    }

    @Async
    @EventListener
    public void clearMeetingData(MeetingCompletedEvent event) {
        Long meetingId = event.getMeetingId();

        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(meetingId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledTasks.remove(meetingId);
        }

        meetingParticipationData.remove(meetingId);
    }

    private void processSegment(long meetingId, DiarizedResponse.Segment segment) {
        if (segment.words() == null || segment.words().isEmpty()) {
            return;
        }

        DiarizedResponse.Word firstWord = segment.words().get(0);
        DiarizedResponse.Word lastWord = segment.words().get(segment.words().size() - 1);

        long participationDuration = firstWord.end() - lastWord.start();
        meetingParticipationData
                .computeIfAbsent(meetingId, k -> new ConcurrentHashMap<>())
                .merge(segment.userId(), participationDuration, Long::sum);
    }

    private void sendParticipationRateUpdate(long meetingId) {
        Map<Long, Long> userParticipationTimes = meetingParticipationData.get(meetingId);

        if (userParticipationTimes == null || userParticipationTimes.isEmpty()) {
            log.info("참여율 데이터가 없어 전송하지 않습니다 - meetingId: {}", meetingId);
            return;
        }

        Map<Long, Double> participationRates = calculateParticipationRate(userParticipationTimes);

        meetingSseService.sendEventToMeeting(
                String.valueOf(meetingId),
                MeetingSseEventType.PARTICIPATION_RATE,
                ParticipationRateRes.of(LocalDateTime.now(), participationRates)
        );
    }

    private static Map<Long, Double> calculateParticipationRate(Map<Long, Long> userParticipationTimes) {
        long totalTime = userParticipationTimes.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        Map<Long, Double> participationRates = new HashMap<>();
        for (Map.Entry<Long, Long> entry : userParticipationTimes.entrySet()) {
            double participationRate = totalTime > 0 ? (double) entry.getValue() / totalTime * 100 : 0.0;
            participationRates.put(entry.getKey(), Math.round(participationRate * 100.0) / 100.0);
        }

        return participationRates;
    }
}
