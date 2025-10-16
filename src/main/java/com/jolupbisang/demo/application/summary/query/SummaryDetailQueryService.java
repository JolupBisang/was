package com.jolupbisang.demo.application.summary.query;

import com.jolupbisang.demo.application.summary.exception.SummaryErrorCode;
import com.jolupbisang.demo.application.summary.query.dto.SummaryListRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.summary.Summary;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.global.exception.NotFoundException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.summary.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryDetailQueryService {

    private final SummaryRepository summaryRepository;
    private final MeetingRepository meetingRepository;

    public Slice<SummaryListRes> getSummaries(Long meetingId, Long accessUserId, boolean isRecap, Pageable pageable) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(() -> new NotFoundException("meetingId: %d", meetingId));

        if (!meeting.isParticipant(accessUserId)) {
            throw new ApplicationException(SummaryErrorCode.NOT_PARTICIPANT, "userId: %d", accessUserId);
        }

        Slice<Summary> summaries = summaryRepository.findByMeetingIdAndIsRecap(meetingId, isRecap, pageable);

        return summaries.map(SummaryListRes::from);
    }

}
