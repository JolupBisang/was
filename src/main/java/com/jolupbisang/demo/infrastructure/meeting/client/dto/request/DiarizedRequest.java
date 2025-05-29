package com.jolupbisang.demo.infrastructure.meeting.client.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.BinaryMessage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public record DiarizedRequest(Dict dict, byte[] audio) {

    private DiarizedRequest(String groupId, String userId, byte[] audio) {
        this(new Dict(WhisperRequestType.DIARIZED, groupId, userId), audio);
    }

    public static DiarizedRequest of(String groupId, String userId, byte[] audio) {
        return new DiarizedRequest(groupId, userId, audio);
    }

    public BinaryMessage toBinaryMessage(ObjectMapper objectMapper) throws IOException {
        String metadata = objectMapper.writeValueAsString(this.dict);

        byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
        int metadataLength = metadataBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(4 + metadataLength + audio.length);
        buffer.putInt(metadataLength);
        buffer.put(metadataBytes);
        buffer.put(audio);

        return new BinaryMessage(buffer.array());
    }

    private record Dict(WhisperRequestType type, String groupId, String userId) {
    }
} 
