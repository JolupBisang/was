package com.jolupbisang.demo.application.segment.command;

import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegmentCreationEventListener {

    private final SegmentCreationService segmentCreationService;

    @RabbitListener(queues = RabbitMQConfig.SEGMENT_QUEUE)
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 10000)
    )
    public void receiveSegmentBatch(List<CompletedSegmentReceivedEvent> messages) {
        log.info("Processing batch of {} segments", messages.size());

        for (CompletedSegmentReceivedEvent message : messages) {
            segmentCreationService.saveAll(message.meetingId(), message.completed());
        }

        log.info("Successfully processed batch of {} segments", messages.size());
    }

}