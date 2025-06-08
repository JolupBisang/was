package com.jolupbisang.demo.infrastructure.audio.client.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public record ReferenceRequest(
        WhisperRequestType flag,
        @JsonProperty("group_id")
        String groupId,
        @JsonProperty("user_ids")
        List<String> userIds,
        List<Integer> counts,

        @JsonIgnore
        List<byte[]> audios

) {

    public static ReferenceRequest of(long groupId, List<Long> userIds, List<Integer> counts, List<byte[]> audios) {
        return new ReferenceRequest(
                WhisperRequestType.REFERENCE,
                String.valueOf(groupId),
                userIds.stream().map(String::valueOf).toList(),
                counts,
                audios
        );
    }

    public BinaryMessage toBinaryMessage(ObjectMapper objectMapper) throws IOException {
        String metadata = objectMapper.writeValueAsString(this);
        byte[] metadataBytes = metadata.getBytes(StandardCharsets.UTF_8);
        int metadataLength = metadataBytes.length;
        int totalAudioBytes = Math.toIntExact(audios.stream().mapToLong(audio -> audio.length).sum());

        ByteBuffer buffer = ByteBuffer.allocate(4 + metadataLength + totalAudioBytes);
        buffer.putInt(metadataLength);
        buffer.put(metadataBytes);
        audios.forEach(buffer::put);

        return new BinaryMessage(buffer.array());
    }
}

