package com.jolupbisang.demo.presentation.meeting;

import com.jolupbisang.demo.application.meeting.service.MeetingService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.meeting.dto.request.MeetingUpdateReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;


    @PutMapping("/{meetingId}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long meetingId,
                                           @Valid @RequestBody MeetingUpdateReq meetingUpdateReq,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        meetingService.updateMeeting(meetingId, userDetails.getUserId(), meetingUpdateReq);
        return ResponseEntity.ok(SuccessResponse.of("회의 정보가 성공적으로 수정되었습니다.", null));
    }
}
