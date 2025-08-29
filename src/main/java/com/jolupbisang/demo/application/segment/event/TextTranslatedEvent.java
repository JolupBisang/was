package com.jolupbisang.demo.application.segment.event;

import com.jolupbisang.demo.infrastructure.whisper.dto.WhisperDiarizedResponse;

import java.util.List;

public record TextTranslatedEvent(
        long meetingId,
        List<SegmentDto> completed,
        List<SegmentDto> candidate
) {
    public static TextTranslatedEvent from(WhisperDiarizedResponse res) {
        return new TextTranslatedEvent(
                res.meetingId(),
                res.completed().stream()
                        .map(TextTranslatedEvent::createSegmentDto)
                        .toList(),
                res.candidate().stream()
                        .map(TextTranslatedEvent::createSegmentDto)
                        .toList()
        );
    }

    private static SegmentDto createSegmentDto(WhisperDiarizedResponse.WhisperSegment whisperSegment) {
        return new SegmentDto(
                whisperSegment.order(),
                whisperSegment.lang(),
                whisperSegment.text(),
                whisperSegment.words().stream()
                        .map(TextTranslatedEvent::createWordDto)
                        .toList(),
                whisperSegment.userId(),
                whisperSegment.audioUserId()
        );
    }

    private static WordDto createWordDto(WhisperDiarizedResponse.WhisperWord whisperWord) {
        return new WordDto(
                whisperWord.start(),
                whisperWord.end(),
                whisperWord.text(),
                whisperWord.lang()
        );
    }
}
