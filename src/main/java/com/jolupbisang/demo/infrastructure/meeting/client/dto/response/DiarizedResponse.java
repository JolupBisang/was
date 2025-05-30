package com.jolupbisang.demo.infrastructure.meeting.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DiarizedResponse(
        List<Segment> completed,
        List<Segment> candidate
) {
    public record Segment(
            int order,
            List<String> lang,
            String text,
            List<Word> words,
            @JsonProperty("user_id")
            String userId,
            @JsonProperty("audio_id")
            String audioUserId
    ) {
    }

    public record Word(
            float start,
            float end,
            String text,
            String lang
    ) {
    }
}
