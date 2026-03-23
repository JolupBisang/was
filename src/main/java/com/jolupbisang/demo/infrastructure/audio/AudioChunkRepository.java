package com.jolupbisang.demo.infrastructure.audio;

import com.jolupbisang.demo.domain.audio.model.AudioChunk;

public interface AudioChunkRepository {

    String save(AudioChunk audioChunk);
}
