package com.jolupbisang.demo.domain.segment.service;

import com.jolupbisang.demo.application.segment.event.SegmentDto;
import com.jolupbisang.demo.application.segment.event.WordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SegmentTimeCalculator {

    private static final double BIT_PER_SECOND = 16000;
    private static int firstSegmentIdx = 0;

    public LocalDateTime calculateSpokenTime(SegmentDto segmentDto, LocalDateTime firstProcessedTime) {
        List<WordDto> words = segmentDto.words();
        if (firstProcessedTime == null || words == null || words.isEmpty()) {
            return firstProcessedTime;
        }

        double offsetSeconds = words.get(firstSegmentIdx).start() / BIT_PER_SECOND;

        return firstProcessedTime.plusSeconds((long) offsetSeconds);
    }
}
