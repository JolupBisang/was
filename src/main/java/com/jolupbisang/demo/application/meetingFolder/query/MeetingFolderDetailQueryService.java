package com.jolupbisang.demo.application.meetingFolder.query;

import com.jolupbisang.demo.application.meetingFolder.exception.MeetingFolderApplicationErrorCode;
import com.jolupbisang.demo.application.meetingFolder.query.dto.MeetingFolderDetailRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.infrastructure.meeting.MeetingRepository;
import com.jolupbisang.demo.infrastructure.meetingFolder.MeetingFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingFolderDetailQueryService {

    private final MeetingFolderRepository meetingFolderRepository;
    private final MeetingRepository meetingRepository;

    @Transactional(readOnly = true)
    public MeetingFolderDetailRes getFolderMeetings(Long userId, Long folderId) {
        MeetingFolder folder = meetingFolderRepository.findByIdWithMeetings(folderId)
                .orElseThrow(() -> new ApplicationException(MeetingFolderApplicationErrorCode.NOT_FOUND_FOLDER, "folderId: %d", folderId));

        folder.validateOwnerAuthority(userId);

        List<Long> meetingIds = folder.getMeetingIds();
        if (meetingIds.isEmpty()) {
            return MeetingFolderDetailRes.from(List.of());
        }

        List<Meeting> meetings = meetingRepository.findAllById(meetingIds);

        List<MeetingFolderDetailRes.MeetingInfo> meetingInfos = meetings.stream()
                .map(MeetingFolderDetailRes.MeetingInfo::fromEntity)
                .toList();

        return MeetingFolderDetailRes.from(meetingInfos);
    }
}

