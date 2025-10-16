package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.domain.audio.model.EmbeddingAudio;

public interface EmbeddingAudioRepository {

    void save(EmbeddingAudio embeddingAudio);
}
