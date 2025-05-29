package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Getter
public class DiarizedRequest {

    private final Dict dict;
    private final byte[] audio;

    private DiarizedRequest(String groupId, String userId, byte[] audio) {
        this.dict = new Dict(WhisperRequestType.DIARIZED, groupId, userId);
        this.audio = audio;
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

    @Getter
    private static class Dict {
        private final WhisperRequestType type;
        private final String groupId;
        private final String userId;

        public Dict(WhisperRequestType type, String groupId, String userId) {
            this.type = type;
            this.groupId = groupId;
            this.userId = userId;
        }
    }
} 