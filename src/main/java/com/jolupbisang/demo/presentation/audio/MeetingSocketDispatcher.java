package com.jolupbisang.demo.presentation.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.audio.service.AudioService;
import com.jolupbisang.demo.presentation.audio.dto.request.SocketRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketDispatcher {

    private final ObjectMapper objectMapper;
    private final AudioService audioService;

    public void dispatchTextMessage(WebSocketSession session, String messagePayload) {
        try {
            SocketRequest requestMessage = objectMapper.readValue(messagePayload, SocketRequest.class);

            switch (requestMessage.type()) {
                case CANCEL_COMPLETION:
                    // TODO: 실제 서비스 로직 호출
                    break;
                default:
                    log.warn("[{}] 처리할 수 없는 메시지 타입입니다: {}", session.getId(), requestMessage.type());
            }
        } catch (Exception e) {
            log.error("[{}] 메시지 처리 중 에러 발생: {}, 페이로드: {}", session.getId(), e.getMessage(), messagePayload, e);
        }
    }

    public void dispatchBinaryMessage(WebSocketSession session, BinaryMessage binaryMessage) throws IOException {
        audioService.processAndSaveAudioData(session, binaryMessage);
    }
} 
