package com.jolupbisang.demo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FeedbackReceivedEvent extends ApplicationEvent {
    private final long meetingId;
    private final long userId;
    private final String comment;

    public FeedbackReceivedEvent(Object source, long meetingId, long userId, String comment) {
        super(source);
        this.meetingId = meetingId;
        this.userId = userId;
        this.comment = comment;
    }
}
