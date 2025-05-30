package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import org.springframework.context.ApplicationEvent;

public class DiarizedReceivedEvent extends ApplicationEvent {
    private final DiarizedResponse diarizedResponse;

    public DiarizedReceivedEvent(DiarizedResponse source) {
        super(source);
        this.diarizedResponse = source;
    }

    public DiarizedResponse getDiarizedResponse() {
        return diarizedResponse;
    }
} 
