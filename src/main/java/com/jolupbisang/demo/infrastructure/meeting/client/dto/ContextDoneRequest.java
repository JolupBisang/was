package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Getter
public class ContextDoneRequest {

    private final Dict dict;

    private ContextDoneRequest(String groupId) {
        this.dict = new Dict(WhisperRequestType.CONTEXT_DONE, groupId);
    }

    public static ContextDoneRequest of(String groupId) {
        return new ContextDoneRequest(groupId);
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