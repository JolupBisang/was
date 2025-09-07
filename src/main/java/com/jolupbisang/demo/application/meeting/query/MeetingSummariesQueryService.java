package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.application.meeting.query.dto.MeetingDetailSummary;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingSummariesQueryService {

    private final MeetingRepository meetingRepository;

    private static final int MIN_YEAR = 0;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int START_DAY_OF_MONTH = 1;
    private static final int START_HOUR_OF_DAY = 0;
    private static final int START_MINUTE_OF_HOUR = 0;
    private static final int START_SECOND_OF_MINUTE = 0;

    @Transactional(readOnly = true)
    public List<MeetingDetailSummary> getMeetingDetailSummary(int year, int month, long userId) {
        if (year < MIN_YEAR || month < MIN_MONTH || month > MAX_MONTH) {
            throw new ApplicationException(MeetingApplicationErrorCode.INVALID_QUERY_DATE, "year: %d, month: %d", year, month);
        }

        LocalDateTime startDayOfMonth = LocalDateTime.of(year, month, START_DAY_OF_MONTH, START_HOUR_OF_DAY, START_MINUTE_OF_HOUR, START_SECOND_OF_MINUTE);
        LocalDateTime endDayOfMonth = startDayOfMonth.plusMonths(1).minusNanos(1);

        List<Meeting> meetings = meetingRepository.findByUserIdAndStartTimeBetween(userId, startDayOfMonth, endDayOfMonth);

        return meetings.stream()
                .map(MeetingDetailSummary::fromEntity)
                .collect(Collectors.toList());
    }
}
