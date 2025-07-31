package com.jolupbisang.demo.application.summary.query;

import com.jolupbisang.demo.application.summary.query.dto.LiveSummaryDto;
import com.jolupbisang.demo.domain.summary.event.SummaryCreatedEvent;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;
import com.jolupbisang.demo.infrastructure.sse.MeetingSseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SummaryCreatedEventListener {

    private final MeetingSseManager sseManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSummaryCreatedEvent(SummaryCreatedEvent event) {
        sseManager.sendEvent(
                event.meetingId(),
                MeetingSseEventType.SUMMARY,
                new LiveSummaryDto(event.content())
        );
    }
}
