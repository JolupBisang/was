package com.jolupbisang.demo.application.event.whisper;

import com.jolupbisang.demo.infrastructure.meeting.client.dto.response.EmbeddedVectorResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WhisperEmbeddedEvent extends ApplicationEvent {
    private final long userId;
    private final byte[] audio;

    public WhisperEmbeddedEvent(EmbeddedVectorResponse embeddedVectorResponse) {
        super(embeddedVectorResponse);
        this.userId = embeddedVectorResponse.userId();
        this.audio = embeddedVectorResponse.audio();
    }
}
