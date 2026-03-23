package com.jolupbisang.demo.application.meetingFolder.query.dto;

import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public record MeetingFolderListRes(
        List<MeetingFolderInfo> folders,
        boolean hasNext
) {
    public static MeetingFolderListRes from(Slice<MeetingFolderInfo> folderSlice) {
        return new MeetingFolderListRes(folderSlice.getContent(), folderSlice.hasNext());
    }

    public record MeetingFolderInfo(
            Long folderId,
            String folderName,
            String meetingName,
            LocalDateTime scheduledStartTime,
            LocalDateTime scheduledEndTime
    ) {
        public static MeetingFolderInfo fromEntity(MeetingFolder folder, Meeting closestMeeting) {
            if (closestMeeting != null) {
                return new MeetingFolderInfo(
                        folder.getId(),
                        folder.getName(),
                        closestMeeting.getTitle(),
                        closestMeeting.getScheduledTime().getScheduledStartTime(),
                        closestMeeting.getScheduledTime().getScheduledEndTime()
                );
            }
            return new MeetingFolderInfo(
                    folder.getId(),
                    folder.getName(),
                    null,
                    null,
                    null
            );
        }
    }
}


