package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.query.MeetingDuplicatedTimeCheckService;
import com.jolupbisang.demo.application.meeting.query.dto.DuplicationCheckRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class MeetingDuplicatedTimeCheckController {

    private final MeetingDuplicatedTimeCheckService meetingDuplicationCheckService;

    @GetMapping("/api/v1/meetings/duplicated")
    public ResponseEntity<DuplicationCheckRes> checkDuplicatedTime(@RequestParam LocalDateTime startTime,
                                                                   @RequestParam Long targetMinutes,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(meetingDuplicationCheckService.checkDuplicatedTime(startTime, targetMinutes, userDetails.getUserId()));
    }
}
