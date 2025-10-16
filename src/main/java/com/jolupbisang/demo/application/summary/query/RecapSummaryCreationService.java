package com.jolupbisang.demo.application.summary.query;

import com.jolupbisang.demo.domain.meeting.event.MeetingCompletedEvent;
import com.jolupbisang.demo.infrastructure.whisper.WhisperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class RecapSummaryCreationService {

    private final WhisperClient whisperClient;

    @Order(3)
    @Async("AsyncTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createWholeSummary(MeetingCompletedEvent event) {
        whisperClient.sendContextDone(event.meetingId());
    }
}
