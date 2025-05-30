package com.jolupbisang.demo.application.event.whisper;

import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.DiarizedResponse;
import org.springframework.context.ApplicationEvent;

public class WhisperDiarizedEvent extends ApplicationEvent {
    private final DiarizedResponse diarizedResponse;

    public WhisperDiarizedEvent(DiarizedResponse source) {
        super(source);
        this.diarizedResponse = source;
    }

    public DiarizedResponse getDiarizedResponse() {
        return diarizedResponse;
    }
} 
