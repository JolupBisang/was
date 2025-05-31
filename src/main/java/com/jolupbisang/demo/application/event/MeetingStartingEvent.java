package com.jolupbisang.demo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MeetingStartingEvent extends ApplicationEvent {
    private final long meetingId;

    public MeetingStartingEvent(Object source, long meetingId) {
        super(source);
        this.meetingId = meetingId;
    }
} 
