package com.jolupbisang.demo.infrastructure.meeting.client.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.BinaryMessage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.io.IOException;

public record ContextDoneRequest(Dict dict) {

    private ContextDoneRequest(String groupId) {
        this(new Dict(WhisperRequestType.CONTEXT_DONE, groupId));
    }

    public static ContextDoneRequest of(String groupId) {
        return new ContextDoneRequest(groupId);
    }

    public TextMessage toTextMessage(ObjectMapper objectMapper) throws IOException {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }

    public BinaryMessage toBinaryMessage(ObjectMapper objectMapper) throws IOException {
        String metadata = objectMapper.writeValueAsString(this.dict);
        byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
        int metadataLength = metadataBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(4 + metadataLength);
        buffer.putInt(metadataLength);
        buffer.put(metadataBytes);

        return new BinaryMessage(buffer.array());
    }

    private record Dict(WhisperRequestType type, String groupId) {
    }
} 
