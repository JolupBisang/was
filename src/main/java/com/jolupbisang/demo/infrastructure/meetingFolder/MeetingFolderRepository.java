package com.jolupbisang.demo.infrastructure.meetingFolder;

import com.jolupbisang.demo.domain.meetingFolder.model.MeetingFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingFolderRepository extends JpaRepository<MeetingFolder, Long>, MeetingFolderRepositoryCustom {
    List<MeetingFolder> findByUserId(Long userId);
}


