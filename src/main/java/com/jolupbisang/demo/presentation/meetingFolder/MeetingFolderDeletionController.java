package com.jolupbisang.demo.presentation.meetingFolder;

import com.jolupbisang.demo.application.meetingFolder.command.MeetingFolderDeletionService;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderDeletionReq;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderDeletionRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingFolderDeletionController {

    private final MeetingFolderDeletionService meetingFolderDeletionService;

    @DeleteMapping("/api/v1/meeting-folders")
    public ResponseEntity<MeetingFolderDeletionRes> deleteFolders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MeetingFolderDeletionReq req) {
        MeetingFolderDeletionRes res = meetingFolderDeletionService.delete(userDetails.getUserId(), req);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(res);
    }
}

