package com.jolupbisang.demo.infrastructure.meeting.client.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WhisperResponseType {

    DIARIZED("diarized"),
    CONTEXT("context");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
