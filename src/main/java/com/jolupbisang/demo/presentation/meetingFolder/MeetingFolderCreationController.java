package com.jolupbisang.demo.presentation.meetingFolder;

import com.jolupbisang.demo.application.meetingFolder.command.MeetingFolderCreationService;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderCreationReq;
import com.jolupbisang.demo.application.meetingFolder.command.dto.MeetingFolderCreationRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingFolderCreationController {

    private final MeetingFolderCreationService meetingFolderCreationService;

    @PostMapping("/api/v1/meeting-folders")
    public ResponseEntity<MeetingFolderCreationRes> createFolder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MeetingFolderCreationReq req) {
        MeetingFolderCreationRes res = meetingFolderCreationService.create(userDetails.getUserId(), req);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }
}

