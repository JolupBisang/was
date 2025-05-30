package com.jolupbisang.demo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MeetingStartingEvent extends ApplicationEvent {
    private final Long meetingId;

    public MeetingStartingEvent(Object source, Long meetingId) {
        super(source);
        this.meetingId = meetingId;
    }
} 