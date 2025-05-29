package com.jolupbisang.demo.application.meeting.event;

import org.springframework.context.ApplicationEvent;

public class MeetingCompletedEvent extends ApplicationEvent {
    private final Long meetingId;

    public MeetingCompletedEvent(Object source, Long meetingId) {
        super(source);
        this.meetingId = meetingId;
    }

    public Long getMeetingId() {
        return meetingId;
    }
} 