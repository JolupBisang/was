package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;

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
