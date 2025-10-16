package com.jolupbisang.demo.application.meeting.command;

import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.domain.meeting.event.MeetingStartedEvent;
import com.jolupbisang.demo.domain.meeting.service.ParticipationRateCalculator;
import com.jolupbisang.demo.infrastructure.participationRate.RealTimeParticipationRepository;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiveParticipationRateService {

    private final RealTimeParticipationRepository realTimeParticipationRepository;
    private final ParticipationRateCalculator participationRateCalculator;
    private final MeetingSseManager sseManager;
    private final TaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Value("${schedule.participation-rate}")
    private long PARTICIPATION_RATE_SEND_INTERVAL_SECONDS;

    @EventListener
    public void sendParticipationRateFromMeetingStarted(MeetingStartedEvent event) {
        long meetingId = event.meetingId();

        Runnable task = () -> sendParticipationRateUpdate(meetingId);

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
                task,
                Instant.now().plusSeconds(PARTICIPATION_RATE_SEND_INTERVAL_SECONDS),
                Duration.ofSeconds(PARTICIPATION_RATE_SEND_INTERVAL_SECONDS));

        scheduledTasks.put(meetingId, scheduledFuture);
    }

    @EventListener
    @Async("AsyncTaskExecutor")
    public void cancelScheduledTask(MeetingCompletedEvent event) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(event.meetingId());
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledTasks.remove(event.meetingId());
        }
    }


    private void sendParticipationRateUpdate(long meetingId) {
        Map<Long, Long> userParticipationTimes = realTimeParticipationRepository.findByMeetingId(meetingId);

        if (userParticipationTimes == null || userParticipationTimes.isEmpty()) {
            log.info("참여율 데이터가 없어 전송하지 않습니다 - meetingId: {}", meetingId);
            return;
        }

        Map<Long, Double> participationRates = participationRateCalculator.calculate(userParticipationTimes);
        sseManager.sendEvent(meetingId, MeetingSseEventType.PARTICIPATION_RATE, participationRates);
    }
}
