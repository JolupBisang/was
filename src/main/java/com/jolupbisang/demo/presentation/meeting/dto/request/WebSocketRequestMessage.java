package com.jolupbisang.demo.presentation.meeting.dto.request;

public record WebSocketRequestMessage (
    MessageType type,
    String body
) {
} 