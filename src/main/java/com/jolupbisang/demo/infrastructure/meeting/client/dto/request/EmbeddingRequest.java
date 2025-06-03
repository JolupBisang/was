package com.jolupbisang.demo.infrastructure.meeting.client.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EmbeddingRequest(
        WhisperRequestType flag,

        @JsonProperty("user_id")
        String userId,

        @JsonIgnore
        byte[] audio
) {
    public static EmbeddingRequest of(long userId, byte[] audioData) {
        return new EmbeddingRequest(WhisperRequestType.EMBEDDING, String.valueOf(userId), audioData);
    }

    public BinaryMessage toBinaryMessage(ObjectMapper objectMapper) throws IOException {
        String metadata = objectMapper.writeValueAsString(this);
        byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
        int metadataLength = metadataBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(4 + metadataLength + audio.length);
        buffer.putInt(metadataLength);
        buffer.put(metadataBytes);
        buffer.put(audio);

        return new BinaryMessage(buffer.array());
    }

}
