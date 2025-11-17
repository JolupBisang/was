package com.jolupbisang.demo.infrastructure.whisper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jolupbisang.demo.infrastructure.audio.client.dto.response.WhisperResponseType;

import java.time.LocalDateTime;
import java.util.List;

public record WhisperDiarizedRes(
        WhisperResponseType flag,
        @JsonProperty("group_id")
        long meetingId,
        List<WhisperSegment> completed,
        List<WhisperSegment> candidate
) {
    public record WhisperSegment(
            int order,
            List<String> lang,
            String text,
            List<WhisperWord> words,
            @JsonProperty("user_id")
            long userId,
            @JsonProperty("audio_id")
            long audioUserId,
            @JsonProperty("translated_time")
            LocalDateTime translatedTime
    ) {
    }

    public record WhisperWord(
            int start,
            int end,
            String text,
            String lang
    ) {
    }
}
