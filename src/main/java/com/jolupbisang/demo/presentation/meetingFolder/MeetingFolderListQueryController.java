package com.jolupbisang.demo.presentation.meetingFolder;

import com.jolupbisang.demo.application.meetingFolder.query.MeetingFolderListQueryService;
import com.jolupbisang.demo.application.meetingFolder.query.dto.MeetingFolderListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingFolderListQueryController {

    private final MeetingFolderListQueryService meetingFolderListQueryService;

    @GetMapping("/api/v1/meeting-folders")
    public ResponseEntity<MeetingFolderListRes> getAllFolders(
            @RequestParam(required = false, defaultValue = "") String name,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20) Pageable pageable) {
        MeetingFolderListRes folders = meetingFolderListQueryService.getFoldersByUserId(name, userDetails.getUserId(), pageable);

        return ResponseEntity.ok(folders);
    }
}

