package com.jolupbisang.demo.application.summary.command;

import com.jolupbisang.demo.domain.summary.Summary;
import com.jolupbisang.demo.infrastructure.summary.SummaryRepository;
import com.jolupbisang.demo.infrastructure.whisper.event.SummaryReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SummaryReceivedEventListener {

    private final SummaryRepository summaryRepository;

    @Transactional
    @EventListener
    public void handleSummaryReceived(SummaryReceivedEvent event) {
        summaryRepository.save(
                new Summary(
                        event.meetingId(),
                        event.content(),
                        event.isRecap(),
                        event.generatedDateTime()
                )
        );
    }
}
