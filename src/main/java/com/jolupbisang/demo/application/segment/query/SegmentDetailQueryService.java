package com.jolupbisang.demo.application.segment.query;

import com.jolupbisang.demo.application.meeting.exception.MeetingNotFoundException;
import com.jolupbisang.demo.application.segment.query.dto.SegmentDetailRes;
import com.jolupbisang.demo.domain.meeting.exception.NotParticipantException;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.segment.model.Segment;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.segment.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SegmentDetailQueryService {

    private final SegmentRepository segmentRepository;
    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public Slice<SegmentDetailRes> getSegmentDetails(long meetingId, long accessUserId, Pageable pageable) {
        Meeting meeting = meetingRepository.findByIdWithParticipant(meetingId)
                .orElseThrow(MeetingNotFoundException::new);

        if (!meeting.isParticipant(accessUserId)) {
            throw new NotParticipantException();
        }

        Slice<Segment> segments = segmentRepository.findByMeetingId(meetingId, pageable);

        return segments.map(SegmentDetailRes::from);
    }

}
