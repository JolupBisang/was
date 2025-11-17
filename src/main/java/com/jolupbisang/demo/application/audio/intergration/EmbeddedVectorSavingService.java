package com.jolupbisang.demo.application.audio.intergration;

import com.jolupbisang.demo.application.event.whisper.WhisperEmbeddedEvent;
import com.jolupbisang.demo.infrastructure.audio.EmbeddedVectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmbeddedVectorSavingService {

    private final EmbeddedVectorRepository embeddedVectorRepository;

    @EventListener
    public void handleWhisperEmbeddedEvent(WhisperEmbeddedEvent event) {
        embeddedVectorRepository.save(event.getUserId(), event.getAudio());
    }
}
