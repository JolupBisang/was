package com.jolupbisang.demo.infrastructure.meeting.client.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DiarizedResponse(
        WhisperResponseType flag,
        @JsonProperty("group_id")
        long groupId,
        List<Segment> completed,
        List<Segment> candidate
) {
    public record Segment(
            int order,
            List<String> lang,
            String text,
            List<Word> words,
            @JsonProperty("user_id")
            long userId,
            @JsonProperty("audio_id")
            long audioUserId
    ) {
    }

    public record Word(
            int start,
            int end,
            String text,
            String lang
    ) {
    }
}
