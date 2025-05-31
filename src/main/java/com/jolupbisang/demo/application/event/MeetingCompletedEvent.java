package com.jolupbisang.demo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MeetingCompletedEvent extends ApplicationEvent {
    private final long meetingId;

    public MeetingCompletedEvent(Object source, long meetingId) {
        super(source);
        this.meetingId = meetingId;
    }
} 
