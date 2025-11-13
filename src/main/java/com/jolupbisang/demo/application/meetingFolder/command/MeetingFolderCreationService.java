package com.jolupbisang.demo.application.meetingFolder.command;

import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderCreationReq;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderCreationRes;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.jolupbisang.demo.infrastructure.meetingFolder.MeetingFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingFolderCreationService {

    private final MeetingFolderRepository meetingFolderRepository;

    @Transactional
    public MeetingFolderCreationRes create(Long userId, MeetingFolderCreationReq req) {
        MeetingFolder meetingFolder = new MeetingFolder(userId, req.name(), null);
        meetingFolderRepository.save(meetingFolder);

        return MeetingFolderCreationRes.of(meetingFolder.getId());
    }
}


