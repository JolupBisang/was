package com.jolupbisang.demo.infrastructure.meeting.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ContextResponse(
        String context,
        List<Integer> agenda,
        Feedback feedback
) {
    public record Feedback(
            @JsonProperty("user_id")
            String userId
    ) {
    }
}
