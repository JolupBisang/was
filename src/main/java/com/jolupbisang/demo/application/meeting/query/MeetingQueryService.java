package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.exception.MeetingApplicationErrorCode;
import com.jolupbisang.demo.application.meeting.query.dto.MeetingDetailSummary;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingQueryService {

    private final MeetingRepository meetingRepository;
    private static final int PAGE_SIZE = 20;
    private static final int MIN_YEAR = 0;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;

    @Transactional(readOnly = true)
    public Slice<MeetingDetailSummary> getMeetings(Long userId, Integer year, Integer month, String title, Pageable pageable) {
        // year, month 유효성 검사
        // 1) month만 단독으로 있는 경우는 허용하지 않음
        // 2) year만 있거나, year와 month가 함께 있거나, 둘 다 없는 경우는 허용
        if (year == null && month != null) {
            throw new ApplicationException(MeetingApplicationErrorCode.INVALID_QUERY_DATE, 
                "month cannot be provided without year");
        }
        
        // year, month가 제공된 경우 범위 검사
        if (year != null) {
            if (year < MIN_YEAR) {
                throw new ApplicationException(MeetingApplicationErrorCode.INVALID_QUERY_DATE, 
                    "invalid year: %d", year);
            }
            if (month != null && (month < MIN_MONTH || month > MAX_MONTH)) {
                throw new ApplicationException(MeetingApplicationErrorCode.INVALID_QUERY_DATE, 
                    "invalid month: %d", month);
            }
        }

        Pageable adjustedPageable = Pageable.ofSize(PAGE_SIZE)
                .withPage(pageable.getPageNumber());

        Slice<Meeting> meetings = meetingRepository.findMeetingsByConditions(
            userId, year, month, title, adjustedPageable
        );

        return meetings.map(MeetingDetailSummary::fromEntity);
    }
}

