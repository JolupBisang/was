package com.jolupbisang.demo.application.event.whisper;

import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.ContextResponse;
import org.springframework.context.ApplicationEvent;

public class WhisperContextEvent extends ApplicationEvent {
    private final ContextResponse contextResponse;

    public WhisperContextEvent(ContextResponse source) {
        super(source);
        this.contextResponse = source;
    }

    public ContextResponse getContextResponse() {
        return contextResponse;
    }

}
