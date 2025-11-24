package com.jolupbisang.demo.application.segment.command;

import com.jolupbisang.demo.application.segment.event.SegmentDto;
import com.jolupbisang.demo.domain.segment.model.Segment;
import com.jolupbisang.demo.domain.segment.service.SegmentTimeCalculator;
import com.jolupbisang.demo.infrastructure.audio.AudioProgressRepository;
import com.jolupbisang.demo.infrastructure.segment.SegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Transactional
    public void saveAllBatch(Map<Long, List<SegmentDto>> segmentsByMeetingId) {
        if (segmentsByMeetingId.isEmpty()) {
            return;
        }

        List<Long> meetingIds = new ArrayList<>(segmentsByMeetingId.keySet());

        Map<Long, LocalDateTime> firstProcessedTimeMap = audioProgressRepository.findFirstProcessedTimes(meetingIds);
        Map<Long, List<Integer>> meetingIdToOrders = buildMeetingIdToOrdersMap(segmentsByMeetingId);
        Map<Long, List<Segment>> existingSegmentsByMeetingId = segmentRepository.findByMeetingIdsAndSegmentOrders(meetingIdToOrders);
        Map<String, Segment> existingSegmentsMap = buildExistingSegmentsMap(existingSegmentsByMeetingId);

        List<Segment> newSegments = processAllSegments(segmentsByMeetingId, existingSegmentsMap, firstProcessedTimeMap);

        if (!newSegments.isEmpty()) {
            segmentRepository.saveAll(newSegments);
        }
    }

    private Map<Long, List<Integer>> buildMeetingIdToOrdersMap(Map<Long, List<SegmentDto>> segmentsByMeetingId) {
        Map<Long, List<Integer>> result = new HashMap<>();

        for (Map.Entry<Long, List<SegmentDto>> entry : segmentsByMeetingId.entrySet()) {
            long meetingId = entry.getKey();
            List<SegmentDto> segments = entry.getValue();

            Set<Integer> orderSet = new HashSet<>();
            for (SegmentDto segment : segments) {
                orderSet.add(segment.order());
            }

            result.put(meetingId, new ArrayList<>(orderSet));
        }

        return result;
    }

    private Map<String, Segment> buildExistingSegmentsMap(Map<Long, List<Segment>> existingSegmentsByMeetingId) {
        Map<String, Segment> result = new HashMap<>();

        for (List<Segment> segments : existingSegmentsByMeetingId.values()) {
            for (Segment segment : segments) {
                String key = segment.getMeetingId() + ":" + segment.getOrder();
                result.put(key, segment);
            }
        }

        return result;
    }

    private List<Segment> processAllSegments(
            Map<Long, List<SegmentDto>> segmentsByMeetingId,
            Map<String, Segment> existingSegmentsMap,
            Map<Long, LocalDateTime> firstProcessedTimeMap
    ) {
        List<Segment> newSegments = new ArrayList<>();

        for (Map.Entry<Long, List<SegmentDto>> entry : segmentsByMeetingId.entrySet()) {
            long meetingId = entry.getKey();
            List<SegmentDto> segments = entry.getValue();
            LocalDateTime firstProcessedTime = firstProcessedTimeMap.get(meetingId);

            for (SegmentDto segmentDto : segments) {
                String key = meetingId + ":" + segmentDto.order();
                Segment existingSegment = existingSegmentsMap.get(key);

                if (existingSegment != null && shouldUpdate(existingSegment, segmentDto)) {
                    updateSegment(existingSegment, segmentDto, firstProcessedTime);
                } else {
                    newSegments.add(createSegment(meetingId, segmentDto, firstProcessedTime));
                }
            }
        }

        return newSegments;
    }

    private Map<Integer, Segment> findByExistingSegment(long meetingId, List<SegmentDto> segments) {
        List<Integer> orders = new ArrayList<>();
        for (SegmentDto segment : segments) {
            int order = segment.order();
            if (!orders.contains(order)) {
                orders.add(order);
            }
        }

        List<Segment> existingSegments = segmentRepository.findByMeetingIdAndSegmentOrders(meetingId, orders);

        Map<Integer, Segment> result = new HashMap<>();
        for (Segment segment : existingSegments) {
            result.put(segment.getOrder(), segment);
        }

        return result;
    }

    private void saveAllOrUpdate(long meetingId, List<SegmentDto> segments, Map<Integer, Segment> existingSegments, LocalDateTime firstProcessedTime) {
        List<Segment> newSegments = new ArrayList<>();

        for (SegmentDto segmentDto : segments) {
            Segment existingSegment = existingSegments.get(segmentDto.order());

            if (existingSegment != null) {
                if (shouldUpdate(existingSegment, segmentDto)) {
                    updateSegment(existingSegment, segmentDto, firstProcessedTime);
                }
            } else {
                newSegments.add(createSegment(meetingId, segmentDto, firstProcessedTime));
            }
        }

        if (!newSegments.isEmpty()) {
            segmentRepository.saveAll(newSegments);
        }
    }

    private boolean shouldUpdate(Segment existingSegment, SegmentDto segmentDto) {
        if (segmentDto.translatedTime() == null) {
            return false;
        }
        LocalDateTime existingTranslatedTime = existingSegment.getTranslatedDateTime();
        return existingTranslatedTime == null ||
                !segmentDto.translatedTime().isBefore(existingTranslatedTime);
    }

    private void updateSegment(Segment segment, SegmentDto segmentDto, LocalDateTime firstProcessedTime) {
        String lang = segmentDto.lang().isEmpty() ? null : segmentDto.lang().get(0);
        LocalDateTime spokenTime = segmentTimeCalculator.calculateSpokenTime(segmentDto, firstProcessedTime);

        segment.update(
                segmentDto.userId(),
                segmentDto.audioUserId(),
                segmentDto.text(),
                lang,
                spokenTime,
                segmentDto.translatedTime()
        );
    }

    private Segment createSegment(long meetingId, SegmentDto segmentDto, LocalDateTime firstProcessedTime) {
        String lang = segmentDto.lang().isEmpty() ? null : segmentDto.lang().get(0);
        LocalDateTime spokenTime = segmentTimeCalculator.calculateSpokenTime(segmentDto, firstProcessedTime);

        return new Segment(
                meetingId,
                segmentDto.userId(),
                segmentDto.audioUserId(),
                segmentDto.order(),
                segmentDto.text(),
                lang,
                spokenTime,
                segmentDto.translatedTime()
        );
    }
}
