package com.jolupbisang.demo.application.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.application.event.MeetingCompletedEvent;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponse;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSessionManager {

    private final Map<WebSocketSession, SessionInfo> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    private record SessionInfo(Long userId, Long meetingId) {
    }

    public void save(WebSocketSession session, Long userId, Long meetingId) {
        SessionInfo sessionInfo = new SessionInfo(userId, meetingId);
        sessions.put(session, sessionInfo);
        log.debug("Session saved: sessionId={}, userId={}, meetingId={}", session.getId(), userId, meetingId);
    }

    public void delete(WebSocketSession session) {
        SessionInfo removed = sessions.remove(session);
        if (removed != null) {
            log.debug("Session deleted: sessionId={}, userId={}, meetingId={}", session.getId(), removed.userId(), removed.meetingId());
        } else {
            log.debug("Session not found for deletion: sessionId={}", session.getId());
        }
    }

    public Optional<Long> getUserIdBySession(WebSocketSession session) {
        return Optional.ofNullable(sessions.get(session)).map(SessionInfo::userId);
    }

    public Optional<Long> getMeetingIdBySession(WebSocketSession session) {
        return Optional.ofNullable(sessions.get(session)).map(SessionInfo::meetingId);
    }

    public Optional<WebSocketSession> findByUserId(Long userId) {
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().userId().equals(userId))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public void deleteByUserId(Long userId) {
        List<WebSocketSession> sessionsToDelete = new ArrayList<>();
        for (Map.Entry<WebSocketSession, SessionInfo> entry : sessions.entrySet()) {
            if (entry.getValue().userId().equals(userId)) {
                sessionsToDelete.add(entry.getKey());
            }
        }
        sessionsToDelete.forEach(this::delete);
    }

    @Order(5)
    @EventListener
    public void closeSessionWhenMeetingEnded(MeetingCompletedEvent event) {
        sessions.entrySet().stream().filter(entry -> entry.getValue().meetingId().equals(event.getMeetingId()))
                .forEach(entry -> {
                    try {
                        entry.getKey().close();
                    } catch (IOException e) {
                        log.error("[WebSocketSession] session close error", e);
                    } finally {
                        sessions.remove(entry.getKey());
                    }
                });

    }

    public void sendTextToParticipants(SocketResponseType type, long meetingId, Object data) {
        sessions.entrySet().stream()
                .filter(entry -> entry.getValue().meetingId().equals(meetingId))
                .forEach(entry -> {
                    try {
                        entry.getKey().sendMessage(new TextMessage(objectMapper.writeValueAsString(SocketResponse.of(type, data))));
                    } catch (IOException e) {
                        log.error("[SessionManager] Failed to send data to userId: {}, meetingId: {}", entry.getValue().meetingId, entry.getValue().userId, e);
                    }
                });
    }
} 

