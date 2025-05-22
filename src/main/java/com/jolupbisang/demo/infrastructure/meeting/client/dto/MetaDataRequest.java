package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;

@Getter
public class MetaDataRequest {

    private final Dict dict;
    private final MeetingMetaData metadata;

    private MetaDataRequest(String userId, String groupId, List<String> agenda, int numPeople, String meetingTopic) {
        this.dict = new Dict(WhisperRequestType.METADATA, userId, groupId);
        this.metadata = new MeetingMetaData(agenda, numPeople, meetingTopic);
    }

    public static MetaDataRequest of(String userId, String groupId, List<String> agenda, int numPeople, String meetingTopic) {
        return new MetaDataRequest(userId, groupId, agenda, numPeople, meetingTopic);
    }

    public TextMessage toTextMessage(ObjectMapper objectMapper) throws IOException {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }

    @Getter
    private static class Dict {
        private final WhisperRequestType type;
        private final String userId;
        private final String groupId;

        public Dict(WhisperRequestType type, String userId, String groupId) {
            this.type = type;
            this.userId = userId;
            this.groupId = groupId;
        }
    }

    @Getter
    private static class MeetingMetaData {
        private final int numPeople;
        private final String meetingTopic;
        private final List<String> agenda;

        public MeetingMetaData(List<String> agenda, int numPeople, String meetingTopic) {
            this.agenda = agenda;
            this.numPeople = numPeople;
            this.meetingTopic = meetingTopic;
        }
    }
}
