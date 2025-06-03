package com.jolupbisang.demo.infrastructure.audio;

public interface EmbeddingAudioRepository {

    void save(long userId, byte[] audio);
}
