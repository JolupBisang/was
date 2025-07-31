package com.jolupbisang.demo.application.feedback.command;

import com.jolupbisang.demo.application.feedback.command.dto.LiveFeedbackDto;
import com.jolupbisang.demo.domain.feedback.event.FeedbackCreatedEvent;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FeedbackCreatedEventListener {

    private final MeetingSseManager sseManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFeedbackCreatedEvent(FeedbackCreatedEvent event) {
        sseManager.sendEvent(
                event.meetingId(),
                event.userId(),
                MeetingSseEventType.FEEDBACK,
                new LiveFeedbackDto(event.comment())
        );
    }
}
