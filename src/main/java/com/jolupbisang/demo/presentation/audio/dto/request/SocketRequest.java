package com.jolupbisang.demo.presentation.audio.dto.request;

public record SocketRequest(
        SocketRequestType type,
        String body
) {
} 
