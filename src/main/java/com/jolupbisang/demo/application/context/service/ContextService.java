package com.jolupbisang.demo.application.context.service;

import com.jolupbisang.demo.application.event.MeetingCompletedEvent;
import com.jolupbisang.demo.application.event.MeetingStartingEvent;
import com.jolupbisang.demo.infrastructure.meeting.client.WhisperClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContextService {

    private final WhisperClient whisperClient;
    private final TaskScheduler taskScheduler;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private static final int CONTEXT_SEND_INTERVAL_MINUTES = 5;

    @Async
    @EventListener
    public void handleMeetingStart(MeetingStartingEvent event) {
        Long meetingId = event.getMeetingId();
        Runnable task = () -> whisperClient.sendContext(meetingId);

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(task, Duration.ofMinutes(CONTEXT_SEND_INTERVAL_MINUTES));
        scheduledTasks.put(meetingId, scheduledFuture);
    }

    @Async
    @EventListener
    public void handleMeetingCompletion(MeetingCompletedEvent event) {
        Long meetingId = event.getMeetingId();
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(meetingId);

        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledTasks.remove(meetingId);
            log.info("Cancelled periodic context sending for completed meeting: {}", meetingId);
        }
    }
} 