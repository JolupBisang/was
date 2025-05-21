package com.jolupbisang.demo.presentation.meeting.dto.request;

public record SocketRequest(
        SocketRequestType type,
        String body
) {
} 
