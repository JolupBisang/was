package com.jolupbisang.demo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SummaryReceivedEvent extends ApplicationEvent {
    private final long meetingId;
    private final String context;

    public SummaryReceivedEvent(Object source, long meetingId, String context) {
        super(source);
        this.meetingId = meetingId;
        this.context = context;
    }
}
