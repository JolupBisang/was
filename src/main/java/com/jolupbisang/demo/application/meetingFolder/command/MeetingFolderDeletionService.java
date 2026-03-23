package com.jolupbisang.demo.application.meetingFolder.command;

import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderDeletionReq;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderDeletionRes;
import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import com.jolupbisang.demo.infrastructure.meetingFolder.MeetingFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingFolderDeletionService {

    private final MeetingFolderRepository meetingFolderRepository;

    @Transactional
    public MeetingFolderDeletionRes delete(Long userId, MeetingFolderDeletionReq req) {
        List<MeetingFolder> folders = meetingFolderRepository.findAllById(req.folderIds());

        List<Long> successFolderIds = new ArrayList<>();
        for (MeetingFolder folder : folders) {
            folder.validateOwnerAuthority(userId);
            meetingFolderRepository.delete(folder);
            successFolderIds.add(folder.getId());
        }

        return MeetingFolderDeletionRes.of(successFolderIds);
    }
}

