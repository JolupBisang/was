package com.jolupbisang.demo.application.event.whisper;

import com.jolupbisang.demo.infrastructure.audio.client.dto.response.DiarizedResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WhisperDiarizedEvent extends ApplicationEvent {
    private final DiarizedResponse diarizedResponse;

    public WhisperDiarizedEvent(DiarizedResponse source) {
        super(source);
        this.diarizedResponse = source;
    }
} 
