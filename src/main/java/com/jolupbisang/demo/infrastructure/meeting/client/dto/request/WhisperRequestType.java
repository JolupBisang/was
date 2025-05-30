package com.jolupbisang.demo.infrastructure.meeting.client.dto.request;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WhisperRequestType {
    DIARIZATION("diarization"),
    CONTEXT("context"),
    CONTEXT_DONE("context_done"),
    METADATA("metadata");

    private final String value;

    WhisperRequestType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
} 
