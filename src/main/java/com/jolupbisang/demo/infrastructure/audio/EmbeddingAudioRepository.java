package com.jolupbisang.demo.infrastructure.audio;

import java.io.IOException;

public interface EmbeddingAudioRepository {

    void save(long userId, byte[] audio) throws IOException;
}
