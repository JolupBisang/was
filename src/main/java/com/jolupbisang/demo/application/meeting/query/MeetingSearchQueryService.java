package com.jolupbisang.demo.application.meeting.query;

import com.jolupbisang.demo.application.meeting.query.dto.MeetingDetailSummary;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingSearchQueryService {

    private final MeetingRepository meetingRepository;
    private static final int PAGE_SIZE = 20;

    @Transactional(readOnly = true)
    public Slice<MeetingDetailSummary> searchMeetingsByTitle(String title, Long userId, Pageable pageable) {
        Pageable adjustedPageable = Pageable.ofSize(PAGE_SIZE)
                .withPage(pageable.getPageNumber());

        Slice<Meeting> meetings = meetingRepository.findByTitleContainingAndUserId(title, userId, adjustedPageable);

        Slice<MeetingDetailSummary> summaries = meetings.map(MeetingDetailSummary::fromEntity);

        return summaries;
    }
}

