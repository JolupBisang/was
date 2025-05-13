package com.jolupbisang.demo.infrastructure.meeting.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;

@Slf4j
@Component
public class WhisperClient {

    public void send(BinaryMessage message) {
        log.info("send to Whisper server, need to impl");
    }
}
