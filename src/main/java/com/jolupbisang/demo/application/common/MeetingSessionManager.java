package com.jolupbisang.demo.application.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MeetingSessionManager {

    private final Map<WebSocketSession, SessionInfo> sessions = new ConcurrentHashMap<>();

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

    public List<WebSocketSession> findAllByMeetingId(Long meetingId) {
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().meetingId().equals(meetingId))
                .map(Map.Entry::getKey)
                .toList();
    }
} 

