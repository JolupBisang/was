package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Getter
public class ContextRequest extends WhisperRequest {
    private final String groupId;

    private ContextRequest(String groupId) {
        super(WhisperRequestType.CONTEXT);
        this.groupId = groupId;
    }

    public static ContextRequest of(String groupId) {
        return new ContextRequest(groupId);
    }

    public TextMessage toTextMessage(ObjectMapper objectMapper) throws IOException {
        return new TextMessage(objectMapper.writeValueAsString(this));
    }
} 
