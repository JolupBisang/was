package com.jolupbisang.demo.application.meetingFolder.query;

import com.jolupbisang.demo.application.meetingFolder.query.dto.MeetingFolderListRes;
import com.jolupbisang.demo.domain.meeting.model.Meeting;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.jolupbisang.demo.infrastructure.meetingFolder.MeetingFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingFolderListQueryService {

    private final MeetingFolderRepository meetingFolderRepository;

    @Transactional(readOnly = true)
    public MeetingFolderListRes getFoldersByUserId(Long userId) {
        List<MeetingFolder> folders = meetingFolderRepository.findByUserId(userId);
        LocalDateTime now = LocalDateTime.now();

        List<MeetingFolderListRes.MeetingFolderInfo> folderInfos = folders.stream()
                .map(folder -> {
                    Meeting closestMeeting = meetingFolderRepository.findClosestMeetingByMeetingIds(folder.getMeetingIds(), now)
                            .orElse(null);
                    return MeetingFolderListRes.MeetingFolderInfo.fromEntity(folder, closestMeeting);
                })
                .toList();

        return MeetingFolderListRes.from(folderInfos);
    }
}


