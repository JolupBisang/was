package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Getter
public class DiarizedRequest extends WhisperRequest {
    private final String groupId;
    private final String userId;
    private final byte[] audio;

    private DiarizedRequest(String groupId, String userId, byte[] audio) {
        super(WhisperRequestType.DIARIZED);
        this.groupId = groupId;
        this.userId = userId;
        this.audio = audio;
    }

    public static DiarizedRequest of(String groupId, String userId, byte[] audio) {
        return new DiarizedRequest(groupId, userId, audio);
    }

    public BinaryMessage toBinary(ObjectMapper objectMapper) throws IOException {
        String metadata = objectMapper.writeValueAsString(Map.of(
            "type", type.name(),
            "groupId", groupId,
            "userId", userId
        ));

        byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
        int metadataLength = metadataBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(4 + metadataLength + audio.length);
        buffer.putInt(metadataLength);
        buffer.put(metadataBytes);
        buffer.put(audio);

        return new BinaryMessage(buffer.array());
    }
} 