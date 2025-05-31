package com.jolupbisang.demo.application.context.service;

import com.jolupbisang.demo.application.event.*;
import com.jolupbisang.demo.application.event.whisper.WhisperContextEvent;
import com.jolupbisang.demo.infrastructure.meeting.client.WhisperClient;
import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.ContextResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContextService {

    private final WhisperClient whisperClient;
    private final TaskScheduler taskScheduler;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private static final int CONTEXT_SEND_INTERVAL_MINUTES = 5;

    @Async("AsyncTaskExecutor")
    @EventListener
    public void handleMeetingStart(MeetingStartingEvent event) {
        Long meetingId = event.getMeetingId();
        Runnable task = () -> whisperClient.sendContext(meetingId);

        ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
                task,
                Instant.now().plusSeconds(60 * CONTEXT_SEND_INTERVAL_MINUTES),
                Duration.ofMinutes(CONTEXT_SEND_INTERVAL_MINUTES));
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

    @EventListener
    public void handleContextReceived(WhisperContextEvent event) {
        ContextResponse contextResponse = event.getContextResponse();
        Object source = event.getSource();
        long meetingId = contextResponse.groupId();
        boolean isRecap = contextResponse.isRecap();

        String context = contextResponse.context();
        if (context != null && !context.isEmpty()) {
            eventPublisher.publishEvent(new SummaryReceivedEvent(source, meetingId, context, isRecap));
        }

        List<Integer> agenda = contextResponse.agenda();
        if (agenda != null && !agenda.isEmpty()) {
            eventPublisher.publishEvent(new AgendaReceivedEvent(source, meetingId, agenda));
        }

        ContextResponse.FeedbackRes feedbackRes = contextResponse.feedback();
        if (feedbackRes != null && feedbackRes.userId() != null && feedbackRes.comment() != null && !feedbackRes.comment().isEmpty()) {
            eventPublisher.publishEvent(new FeedbackReceivedEvent(source, meetingId, feedbackRes.userId(), feedbackRes.comment()));
        }
    }
} 
