package com.jolupbisang.demo.application.segment.command;

import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.application.segment.event.SegmentDto;
import com.jolupbisang.demo.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Long, List<SegmentDto>> segmentsByMeetingId = groupSegmentsByMeetingId(messages);
        segmentCreationService.saveAllBatch(segmentsByMeetingId);
    }

    private Map<Long, List<SegmentDto>> groupSegmentsByMeetingId(List<CompletedSegmentReceivedEvent> events) {
        Map<Long, List<SegmentDto>> result = new HashMap<>();

        for (CompletedSegmentReceivedEvent event : events) {
            long meetingId = event.meetingId();
            List<SegmentDto> segments = event.completed();

            result.computeIfAbsent(meetingId, k -> new ArrayList<>()).addAll(segments);
        }

        return result;
    }

}
