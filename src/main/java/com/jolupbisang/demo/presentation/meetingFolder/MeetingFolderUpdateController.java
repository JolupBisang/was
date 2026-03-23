package com.jolupbisang.demo.presentation.meetingFolder;

import com.jolupbisang.demo.application.meetingFolder.command.MeetingFolderUpdateService;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderUpdateReq;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderUpdateRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingFolderUpdateController {

    private final MeetingFolderUpdateService meetingFolderUpdateService;

    @PostMapping("/api/v1/meeting-folders/{folderId}/meetings")
    public ResponseEntity<MeetingFolderUpdateRes> addMeetings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long folderId,
            @Valid @RequestBody MeetingFolderUpdateReq req) {
        MeetingFolderUpdateRes res = meetingFolderUpdateService.addMeetings(userDetails.getUserId(), folderId, req);

        return ResponseEntity
                .ok(res);
    }

    @DeleteMapping("/api/v1/meeting-folders/{folderId}/meetings")
    public ResponseEntity<MeetingFolderUpdateRes> removeMeetings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long folderId,
            @Valid @RequestBody MeetingFolderUpdateReq req) {
        MeetingFolderUpdateRes res = meetingFolderUpdateService.removeMeetings(userDetails.getUserId(), folderId, req);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(res);
    }
}

