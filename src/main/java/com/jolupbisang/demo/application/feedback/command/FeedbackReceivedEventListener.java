package com.jolupbisang.demo.application.feedback.command;

import com.jolupbisang.demo.domain.feedback.Feedback;
import com.jolupbisang.demo.infrastructure.feedback.FeedbackRepository;
import com.jolupbisang.demo.infrastructure.whisper.event.FeedbackReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FeedbackReceivedEventListener {

    private final FeedbackRepository feedbackRepository;

    @EventListener
    @Transactional
    public void handleFeedbackEvent(FeedbackReceivedEvent event) {
        feedbackRepository.save(
                new Feedback(
                        event.userId(),
                        event.meetingId(),
                        event.comment(),
                        event.generatedDateTime()
                ));
    }
}
