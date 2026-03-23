package com.jolupbisang.demo.presentation.audio;

import com.jolupbisang.demo.application.audio.query.FullAudioListQueryService;
import com.jolupbisang.demo.application.audio.query.dto.AudioListRes;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FullAudioListQueryController {

    private final FullAudioListQueryService fullAudioListQueryService;

    @GetMapping("/api/v1/meetings/{meetingId}/fullAudio")
    public ResponseEntity<AudioListRes> getCompletedMeetingAudioList(@PathVariable("meetingId") Long meetingId,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(fullAudioListQueryService.getCompletedMeetingAudioList(meetingId, userDetails.getUserId()));
    }
}
