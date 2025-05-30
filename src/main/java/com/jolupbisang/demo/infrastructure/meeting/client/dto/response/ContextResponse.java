package com.jolupbisang.demo.infrastructure.meeting.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ContextResponse(
        @JsonProperty("group_id")
        long groupId,
        String context,
        List<Integer> agenda,
        FeedbackRes feedback
) {
    public record FeedbackRes(
            @JsonProperty("user_id")
            Long userId,
            String comment
    ) {
    }
}
