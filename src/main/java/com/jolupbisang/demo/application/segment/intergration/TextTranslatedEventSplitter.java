package com.jolupbisang.demo.application.segment.intergration;

import com.jolupbisang.demo.application.segment.event.CandidateSegmentReceivedEvent;
import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.application.segment.event.TextTranslatedEvent;
import com.jolupbisang.demo.global.event.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TextTranslatedEventSplitter {

    @EventListener
    public void splitDiarizedEvent(TextTranslatedEvent event) {
        publishCandidateSegmentsEvent(event);
        publishCompletedSegmentsEvent(event);
    }

    private void publishCandidateSegmentsEvent(TextTranslatedEvent event) {
        if (event.candidate() == null || event.candidate().isEmpty()) {
            return;
        }

        Events.raise(new CandidateSegmentReceivedEvent(
                event.meetingId(),
                event.candidate()
        ));
    }

    private void publishCompletedSegmentsEvent(TextTranslatedEvent event) {
        if (event.completed() == null || event.completed().isEmpty()) {
            return;
        }

        Events.raise(new CompletedSegmentReceivedEvent(
                event.meetingId(),
                event.completed()
        ));
    }
}
