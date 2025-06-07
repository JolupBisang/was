package com.jolupbisang.demo.presentation.audio;


import com.jolupbisang.demo.application.audio.dto.AudioListResponse;
import com.jolupbisang.demo.application.audio.service.AudioService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.audio.api.AudioControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audio")
public class AudioController implements AudioControllerApi {
    private final AudioService audioService;

    @Override
    @PostMapping(value = "/embedding", consumes = "multipart/form-data")
    public ResponseEntity<?> embeddingAudio(@RequestPart("audioFile") MultipartFile file,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        audioService.embeddingAudio(userDetails.getUserId(), file);

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of("전송 성공", null));
    }

    @Override
    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<SuccessResponse<AudioListResponse>> getCompletedMeetingAudioList(
            @PathVariable Long meetingId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        AudioListResponse audioList = audioService.getCompletedMeetingAudioList(meetingId, userDetails.getUserId());

        return ResponseEntity.ok(SuccessResponse.of("오디오 목록 조회 성공", audioList));
    }
}
