package com.jolupbisang.demo.presentation.audio;


import com.jolupbisang.demo.application.audio.service.AudioService;
import com.jolupbisang.demo.global.response.SuccessResponse;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import com.jolupbisang.demo.presentation.audio.api.AudioControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping(value = "/embedding", consumes = "multipart/form-data")
    public ResponseEntity<?> embeddingAudio(@RequestPart("audioFile") MultipartFile file,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        audioService.embeddingAudio(userDetails.getUserId(), file);

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of("전송 성공", null));
    }
}
