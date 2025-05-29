package com.jolupbisang.demo.infrastructure.meeting.client.dto.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.BinaryMessage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;

public record MetaDataRequest(Dict dict, MeetingMetaData metadata) {

    private MetaDataRequest(String userId, String groupId, List<String> agenda, int numPeople, String meetingTopic) {
        this(new Dict(WhisperRequestType.METADATA, userId, groupId),
             new MeetingMetaData(agenda, numPeople, meetingTopic));
    }

    public static MetaDataRequest of(String userId, String groupId, List<String> agenda, int numPeople, String meetingTopic) {
        return new MetaDataRequest(userId, groupId, agenda, numPeople, meetingTopic);
    }

    public TextMessage toTextMessage(ObjectMapper objectMapper) throws IOException {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }

    public BinaryMessage toBinaryMessage(ObjectMapper objectMapper) throws IOException {
        String jsonPayload = objectMapper.writeValueAsString(this);
        byte[] jsonBytes = jsonPayload.getBytes(StandardCharsets.UTF_8);
        int jsonLength = jsonBytes.length;

        ByteBuffer buffer = ByteBuffer.allocate(4 + jsonLength);
        buffer.putInt(jsonLength);
        buffer.put(jsonBytes);

        return new BinaryMessage(buffer.array());
    }

    private record Dict(WhisperRequestType type, String userId, String groupId) {
    }

    private record MeetingMetaData(List<String> agenda, int numPeople, String meetingTopic) {
    }
}
