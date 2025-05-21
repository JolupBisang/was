package com.jolupbisang.demo.infrastructure.meeting.client.dto;

import lombok.Getter;

@Getter
public abstract class WhisperRequest {
    protected final WhisperRequestType type;

    protected WhisperRequest(WhisperRequestType type) {
        this.type = type;
    }
} 