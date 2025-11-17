package com.jolupbisang.demo.application.meetingFolder.query;

import com.jolupbisang.demo.application.meetingFolder.query.dto.MeetingFolderListRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.jolupbisang.demo.infrastructure.meetingFolder.MeetingFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeetingFolderListQueryService {

    private final MeetingFolderRepository meetingFolderRepository;

    @Transactional(readOnly = true)
    public MeetingFolderListRes getFoldersByUserId(String name, Long userId, Pageable pageable) {
        Slice<MeetingFolder> folderSlice = meetingFolderRepository.findByNameContainingAndUserId(name, userId, pageable);
        LocalDateTime now = LocalDateTime.now();

        Slice<MeetingFolderListRes.MeetingFolderInfo> folderInfoSlice = folderSlice.map(folder -> {
            Meeting closestMeeting = meetingFolderRepository.findClosestMeetingByMeetingIds(folder.getMeetingIds(), now)
                    .orElse(null);
            return MeetingFolderListRes.MeetingFolderInfo.fromEntity(folder, closestMeeting);
        });

        return MeetingFolderListRes.from(folderInfoSlice);
    }
}


