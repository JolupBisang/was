package com.jolupbisang.demo.application.segment.command;

import com.jolupbisang.demo.application.segment.event.SegmentDto;
import com.jolupbisang.demo.domain.segment.model.Segment;
import com.jolupbisang.demo.domain.segment.model.Word;
import com.jolupbisang.demo.domain.segment.service.SegmentTimeCalculator;
import com.jolupbisang.demo.infrastructure.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.segment.SegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentCreationService {

    private final SegmentRepository segmentRepository;
    private final SegmentTimeCalculator segmentTimeCalculator;
    private final AudioProgressRepository audioProgressRepository;

    @Transactional
    public void saveAll(long meetingId, List<SegmentDto> segments) {
        Map<Integer, Segment> existingSegments = findByExistingSegment(meetingId, segments);

        LocalDateTime firstProcessedTime = audioProgressRepository.findFirstProcessedTime(meetingId)
                .orElse(null);

        saveAllOrUpdate(meetingId, segments, existingSegments, firstProcessedTime);
    }

    private Map<Integer, Segment> findByExistingSegment(long meetingId, List<SegmentDto> segments) {
        List<Integer> orders = segments.stream()
                .map(SegmentDto::order)
                .toList();

        List<Segment> existingSegments = segmentRepository.findByMeetingIdAndSegmentOrders(meetingId, orders);

        return existingSegments.stream()
                .collect(Collectors.toMap(Segment::getOrder, segment -> segment));
    }

    private void saveAllOrUpdate(long meetingId, List<SegmentDto> segments, Map<Integer, Segment> existingSegments, LocalDateTime firstProcessedTime) {
        List<Segment> newSegments = new ArrayList<>();
        for (SegmentDto segmentDto : segments) {
            if (existingSegments.containsKey(segmentDto.order())) {
                updateSegment(existingSegments.get(segmentDto.order()), segmentDto, firstProcessedTime);
            } else {
                newSegments.add(createSegment(meetingId, segmentDto, firstProcessedTime));
            }
        }

        segmentRepository.saveAll(newSegments);
    }

    private void updateSegment(Segment segment, SegmentDto segmentDto, LocalDateTime firstProcessedTime) {
        if (segmentDto.translatedTime().isBefore(segment.getTranslatedDateTime())) {
            return;
        }

        segment.update(
                segmentDto.userId(),
                segmentDto.audioUserId(),
                segmentDto.words().stream()
                        .map(wordDto -> new Word(
                                wordDto.start(),
                                wordDto.end(),
                                wordDto.text(),
                                wordDto.lang().isEmpty() ? null : wordDto.lang())
                        )
                        .toList(),
                segmentDto.text(),
                segmentDto.lang().isEmpty() ? null : segmentDto.lang().get(0),
                segmentTimeCalculator.calculateSpokenTime(segmentDto, firstProcessedTime),
                segmentDto.translatedTime()
        );
    }

    private Segment createSegment(long meetingId, SegmentDto segmentDto, LocalDateTime firstProcessedTime) {
        return new Segment(
                meetingId,
                segmentDto.userId(),
                segmentDto.audioUserId(),
                segmentDto.order(),
                segmentDto.words().stream()
                        .map(wordDto -> new Word(
                                wordDto.start(),
                                wordDto.end(),
                                wordDto.text(),
                                wordDto.lang().isEmpty() ? null : wordDto.lang())
                        )
                        .toList(),
                segmentDto.text(),
                segmentDto.lang().isEmpty() ? null : segmentDto.lang().get(0),
                segmentTimeCalculator.calculateSpokenTime(segmentDto, firstProcessedTime),
                segmentDto.translatedTime()
        );
    }
}
