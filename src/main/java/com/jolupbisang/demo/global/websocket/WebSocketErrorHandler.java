package com.jolupbisang.demo.global.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.global.exception.ApplicationException;
import com.jolupbisang.demo.global.exception.DomainException;
import com.jolupbisang.demo.global.exception.InfraException;
import com.jolupbisang.demo.global.response.ErrorResponse;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketErrorHandler {

    private final ObjectMapper objectMapper;

    /**
     * 리턴값이 없는 WebSocket 작업을 에러 처리와 함께 실행
     * 오류 발생시 false 반환
     */
    public boolean handleWithErrorManagement(WebSocketSession session, Runnable operation) {
        try {
            operation.run();
        } catch (ApplicationException e) {
            handleApplicationException(session, e);
            return false;
        } catch (DomainException e) {
            handleDomainException(session, e);
            return false;
        } catch (InfraException e) {
            handleInfraException(session, e);
            return false;
        } catch (Exception e) {
            handleUnexpectedException(session, e);
            return false;
        }

        return true;
    }

    /**
     * 리턴값이 있는 WebSocket 작업을 에러 처리와 함께 실행
     */
    public <T> T handleWithErrorManagement(WebSocketSession session, Supplier<T> operation) {
        try {
            return operation.get();
        } catch (ApplicationException e) {
            handleApplicationException(session, e);
            return null;
        } catch (DomainException e) {
            handleDomainException(session, e);
            return null;
        } catch (InfraException e) {
            handleInfraException(session, e);
            return null;
        } catch (Exception e) {
            handleUnexpectedException(session, e);
            return null;
        }
    }

    private void handleApplicationException(WebSocketSession session, ApplicationException e) {
        log.info("Application exception in WebSocket session {}: {}", session.getId(), e.getMessage(), e);
        sendErrorToClient(session, e.getMessage(), e.getErrorCode().getCode());
    }

    private void handleDomainException(WebSocketSession session, DomainException e) {
        log.info("Domain exception in WebSocket session {}: {}", session.getId(), e.getMessage(), e);
        sendErrorToClient(session, e.getMessage(), e.getErrorCode().getCode());
    }

    private void handleInfraException(WebSocketSession session, InfraException e) {
        log.error("Infrastructure exception in WebSocket session {}: {}", session.getId(), e.getMessage(), e);
        sendErrorToClient(session, "내부 시스템 오류가 발생했습니다", e.getErrorCode().getCode());
    }

    private void handleUnexpectedException(WebSocketSession session, Exception e) {
        log.error("Unexpected exception in WebSocket session {}: {}", session.getId(), e.getMessage(), e);
        sendErrorToClient(session, "내부 시스템 오류가 발생했습니다", "UNEXPECTED_ERROR");
    }

    private void sendErrorToClient(WebSocketSession session, String errorMessage, String errorId) {
        if (!session.isOpen()) {
            log.warn("Cannot send error to closed session: {}", session.getId());
            return;
        }

        try {
            SocketResponse<ErrorResponse> errorResponse = SocketResponse.error(errorMessage, errorId);
            String json = objectMapper.writeValueAsString(errorResponse);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.error("Failed to send error message to WebSocket session {}: {}",
                    session.getId(), e.getMessage(), e);
        }
    }
}
