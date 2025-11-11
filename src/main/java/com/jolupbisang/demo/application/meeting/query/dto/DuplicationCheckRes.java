package com.jolupbisang.demo.application.meeting.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public record DuplicationCheckRes(
        List<MeetingDetail> meetings

) {
    public static DuplicationCheckRes of(List<Meeting> existedMeeting) {
        return new DuplicationCheckRes(existedMeeting.stream()
                .map(meeting -> new MeetingDetail(
                        meeting.getTitle(),
                        meeting.getScheduledTime().getScheduledStartTime(),
                        meeting.getScheduledTime().getScheduledEndTime())
                )
                .toList()
        );
    }

    private record MeetingDetail(
            String title,
            LocalDateTime scheduledStartTime,
            LocalDateTime scheduledEndTime
    ) {
    }
}
