package com.jolupbisang.demo.presentation.audio;

import com.jolupbisang.demo.application.audio.command.EmbeddingAudioCreationService;
import com.jolupbisang.demo.infrastructure.auth.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class EmbeddingAudioCreationController {

    private final EmbeddingAudioCreationService embeddingAudioCreationService;

    @PostMapping("/api/v1/audio/embedding")
    public ResponseEntity<Void> createEmbeddingAudio(@RequestPart("audioFile") MultipartFile file,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        embeddingAudioCreationService.createEmbeddingAudio(userDetails.getUserId(), file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
