package com.jolupbisang.demo.application.segment.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SegmentDto(
        int order,
        List<String> lang,
        String text,
        List<WordDto> words,
        @JsonProperty("user_id")
        long userId,
        @JsonProperty("audio_id")
        long audioUserId
) {
}
