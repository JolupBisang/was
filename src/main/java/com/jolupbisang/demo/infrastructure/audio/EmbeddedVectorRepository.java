package com.jolupbisang.demo.infrastructure.audio;

import java.util.List;

public interface EmbeddedVectorRepository {

    void save(long userId, byte[] audio);

    List<byte[]> findAllByUserId(long userId);
}
