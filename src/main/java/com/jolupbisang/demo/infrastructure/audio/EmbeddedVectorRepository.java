package com.jolupbisang.demo.infrastructure.audio;

import java.io.IOException;
import java.util.List;

public interface EmbeddedVectorRepository {

    void save(long userId, byte[] audio) throws IOException;

    List<byte[]> findAllByUserId(long userId);
}
