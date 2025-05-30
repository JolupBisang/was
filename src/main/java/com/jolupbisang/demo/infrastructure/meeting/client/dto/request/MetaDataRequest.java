package com.jolupbisang.demo.infrastructure.meeting.client.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.BinaryMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MetaDataRequest(
        WhisperRequestType flag,

        @JsonProperty("group_id")
        String groupId,

        List<String> agenda,

        @JsonProperty("num_people")
        Long numPeople,

        @JsonProperty("meeting_topic")
        String meetingTopic
) {

    public static MetaDataRequest of(long groupId, List<String> agenda, Long numPeople, String meetingTopic) {
        List<String> tempAgenda = agenda == null || agenda.isEmpty() ? null : agenda;

        return new MetaDataRequest(
                WhisperRequestType.METADATA,
                String.valueOf(groupId),
                tempAgenda,
                numPeople,
                meetingTopic
        );
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
}
