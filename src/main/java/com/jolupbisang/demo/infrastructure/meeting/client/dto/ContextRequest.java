package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.BinaryMessage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.io.IOException;

@Getter
public class ContextRequest {

    private final Dict dict;

    private ContextRequest(String groupId) {
        this.dict = new Dict(WhisperRequestType.CONTEXT, groupId);
    }

    public static ContextRequest of(String groupId) {
        return new ContextRequest(groupId);
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

    @Getter
    private static class Dict {
        private final WhisperRequestType type;
        private final String groupId;

        public Dict(WhisperRequestType type, String groupId) {
            this.type = type;
            this.groupId = groupId;
        }
    }
} 
