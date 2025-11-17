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
        // year, month 유효성 검사 (둘 다 있거나 둘 다 없어야 함)
        if ((year != null && month == null) || (year == null && month != null)) {
            throw new ApplicationException(MeetingApplicationErrorCode.INVALID_QUERY_DATE, 
                "year and month must be both present or both absent");
        }
        
        // year, month가 제공된 경우 범위 검사
        if (year != null && month != null) {
            if (year < MIN_YEAR || month < MIN_MONTH || month > MAX_MONTH) {
                throw new ApplicationException(MeetingApplicationErrorCode.INVALID_QUERY_DATE, 
                    "year: %d, month: %d", year, month);
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

