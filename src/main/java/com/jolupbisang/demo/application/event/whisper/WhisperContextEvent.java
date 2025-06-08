package com.jolupbisang.demo.application.event.whisper;

import com.jolupbisang.demo.infrastructure.audio.client.dto.response.ContextResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WhisperContextEvent extends ApplicationEvent {
    private final ContextResponse contextResponse;

    public WhisperContextEvent(ContextResponse source) {
        super(source);
        this.contextResponse = source;
    }
}
