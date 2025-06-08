package com.jolupbisang.demo.infrastructure.audio.client.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public record ContextDoneRequest(
        WhisperRequestType flag,
        @JsonProperty("group_id")
        String groupId
) {

    public static ContextDoneRequest of(long groupId) {
        return new ContextDoneRequest(WhisperRequestType.CONTEXT_DONE, String.valueOf(groupId));
    }

    public BinaryMessage toBinaryMessage(ObjectMapper objectMapper) throws IOException {
        String metadata = objectMapper.writeValueAsString(this);
        byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
        int metadataLength = metadataBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(4 + metadataLength);
        buffer.putInt(metadataLength);
        buffer.put(metadataBytes);

        return new BinaryMessage(buffer.array());
    }
} 
