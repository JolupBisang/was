package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Getter
public class ContextDoneRequest extends WhisperRequest {
    private final String groupId;

    private ContextDoneRequest(String groupId) {
        super(WhisperRequestType.CONTEXT_DONE);
        this.groupId = groupId;
    }

    public static ContextDoneRequest of(String groupId) {
        return new ContextDoneRequest(groupId);
    }

    public TextMessage toTextMessage(ObjectMapper objectMapper) throws IOException {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }
} 