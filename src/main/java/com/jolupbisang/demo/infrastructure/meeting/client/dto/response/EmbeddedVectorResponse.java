package com.jolupbisang.demo.infrastructure.meeting.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EmbeddedVectorResponse(
        WhisperResponseType flag,
        @JsonProperty("user_id")
        long userId,
        @JsonIgnore
        byte[] audio
) {
    public EmbeddedVectorResponse withAudio(byte[] audio) {
        return new EmbeddedVectorResponse(flag, userId, audio);
    }

}
