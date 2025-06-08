package com.jolupbisang.demo.infrastructure.audio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ContextResponse(
        String flag,
        @JsonProperty("group_id")
        long groupId,
        @JsonProperty("is_recap")
        boolean isRecap,
        String context,
        List<Integer> agenda,
        List<FeedbackRes> feedback
) {
    public record FeedbackRes(
            @JsonProperty("user_id")
            Long userId,
            String comment
    ) {
    }
}
