package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.ContextResponse;
import org.springframework.context.ApplicationEvent;

public class ContextReceivedEvent extends ApplicationEvent {
    private final ContextResponse contextResponse;

    public ContextReceivedEvent(ContextResponse source) {
        super(source);
        this.contextResponse = source;
    }

    public ContextResponse getContextResponse() {
        return contextResponse;
    }

}