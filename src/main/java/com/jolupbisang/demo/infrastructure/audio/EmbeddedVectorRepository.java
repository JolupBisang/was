package com.jolupbisang.demo.infrastructure.audio;

import java.io.IOException;

public interface EmbeddedVectorRepository {

    void save(long userId, byte[] audio) throws IOException;
}
