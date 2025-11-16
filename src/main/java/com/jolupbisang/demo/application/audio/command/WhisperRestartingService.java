package com.jolupbisang.demo.application.audio.command;

import com.jolupbisang.demo.application.audio.command.dto.WhisperRestartRes;
import com.jolupbisang.demo.infrastructure.user.UserRepository;
import com.jolupbisang.demo.infrastructure.whisper.WhisperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WhisperRestartingService {

    private final UserRepository userRepository;
    private final WhisperClient whisperClient;

    public WhisperRestartRes restart() {

        return new WhisperRestartRes(whisperClient.connectToWhisperServer());
    }
}
