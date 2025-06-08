package com.jolupbisang.demo.infrastructure.audio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WhisperResponseType {
    EMBEDDED("embedded"),
    DIARIZED("diarized"),
    CONTEXT("context");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
}
