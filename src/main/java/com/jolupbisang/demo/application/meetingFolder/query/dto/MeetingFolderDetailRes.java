package com.jolupbisang.demo.application.meetingFolder.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingFolderDetailRes(
        List<MeetingInfo> meetings
) {
    public static MeetingFolderDetailRes from(List<MeetingInfo> meetingInfos) {
        return new MeetingFolderDetailRes(meetingInfos);
    }

    public record MeetingInfo(
            Long meetingId,
            String title,
            LocalDateTime scheduledStartTime
    ) {
        public static MeetingInfo fromEntity(Meeting meeting) {
            return new MeetingInfo(
                    meeting.getId(),
                    meeting.getTitle(),
                    meeting.getScheduledTime().getScheduledStartTime()
            );
        }
    }
}

