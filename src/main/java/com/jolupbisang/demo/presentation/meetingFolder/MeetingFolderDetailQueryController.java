package com.jolupbisang.demo.presentation.meetingFolder;

import com.jolupbisang.demo.application.meetingFolder.query.MeetingFolderDetailQueryService;
import com.jolupbisang.demo.application.meetingFolder.query.dto.MeetingFolderDetailRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingFolderDetailQueryController {

    private final MeetingFolderDetailQueryService meetingFolderDetailQueryService;

    @GetMapping("/api/v1/meeting-folders/{folderId}/meetings")
    public ResponseEntity<MeetingFolderDetailRes> getFolderMeetings(
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        MeetingFolderDetailRes res = meetingFolderDetailQueryService.getFolderMeetings(userDetails.getUserId(), folderId);
        return ResponseEntity.ok(res);
    }
}

