package com.jolupbisang.demo.presentation.audio;

import com.jolupbisang.demo.application.audio.command.WhisperRestartingService;
import com.jolupbisang.demo.application.audio.command.dto.WhisperRestartRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WhisperRestartingController {

    private final WhisperRestartingService restartingService;

    @PostMapping("/api/v1/whisper/restart")
    public ResponseEntity<WhisperRestartRes> restartWhisperSession() {

        return ResponseEntity.ok(restartingService.restart());
    }
}
