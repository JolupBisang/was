package com.jolupbisang.demo.application.meetingFolder.command;

import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderUpdateReq;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderUpdateRes;
import com.jolupbisang.demo.application.meetingFolder.exception.MeetingFolderApplicationErrorCode;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingFolder.MeetingFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingFolderUpdateService {

    private final MeetingFolderRepository meetingFolderRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public MeetingFolderUpdateRes addMeetings(Long userId, Long folderId, MeetingFolderUpdateReq req) {
        MeetingFolder folder = meetingFolderRepository.findById(folderId)
                .orElseThrow(() -> new ApplicationException(MeetingFolderApplicationErrorCode.NOT_FOUND_FOLDER, "folderId: %d", folderId));

        folder.validateOwnerAuthority(userId);

        List<Meeting> meetings = meetingRepository.findByIdsWithParticipant(req.meetingIds());

        if (meetings.size() != req.meetingIds().size()) {
            throw new ApplicationException(MeetingFolderApplicationErrorCode.NOT_FOUND_MEETING);
        }

        List<Long> successMeetingIds = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if (!meeting.isParticipant(userId)) {
                throw new ApplicationException(MeetingFolderApplicationErrorCode.NOT_MEETING_PARTICIPANT, "meetingId: %d, userId: %d", meeting.getId(), userId);
            }

            if (!folder.hasMeeting(meeting.getId())) {
                folder.addMeeting(meeting.getId());
                successMeetingIds.add(meeting.getId());
            }
        }

        return MeetingFolderUpdateRes.of(successMeetingIds);
    }

    @Transactional
    public MeetingFolderUpdateRes removeMeetings(Long userId, Long folderId, MeetingFolderUpdateReq req) {
        MeetingFolder folder = meetingFolderRepository.findByIdWithMeetings(folderId)
                .orElseThrow(() -> new ApplicationException(MeetingFolderApplicationErrorCode.NOT_FOUND_FOLDER, "folderId: %d", folderId));

        folder.validateOwnerAuthority(userId);

        List<Long> successMeetingIds = new ArrayList<>();
        for (Long meetingId : req.meetingIds()) {
            if (folder.hasMeeting(meetingId)) {
                folder.removeMeeting(meetingId);
                successMeetingIds.add(meetingId);
            }
        }

        return MeetingFolderUpdateRes.of(successMeetingIds);
    }
}


