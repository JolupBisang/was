package com.jolupbisang.demo.presentation.meeting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.meeting.service.AudioService; // AudioService는 dispatcher로 이동하거나 dispatcher에서 호출
import com.jolupbisang.demo.presentation.meeting.dto.request.WebSocketRequestMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSocketDispatcher {

    private final ObjectMapper objectMapper;
    private final AudioService audioService;

    public void dispatchTextMessage(WebSocketSession session, String messagePayload) {
        try {
            WebSocketRequestMessage requestMessage = objectMapper.readValue(messagePayload, WebSocketRequestMessage.class);

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