package com.jolupbisang.demo.infrastructure.meeting.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MeetingSessionRepositoryImpl implements MeetingSessionRepository {

    private final Map<String, SessionInfo> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void save(WebSocketSession session, Long userId, Long meetingId) {
        String sessionId = session.getId();
        sessionMap.put(sessionId, new SessionInfo(userId, meetingId));
        log.info("Session saved: sessionId={}, userId={}, meetingId={}", sessionId, userId, meetingId);
    }

    @Override
    public void delete(WebSocketSession session) {
        SessionInfo removed = sessionMap.remove(session.getId());
        if (removed != null) {
            log.info("Session deleted: sessionId={}, userId={}, meetingId={}", session.getId(), removed.userId(), removed.meetingId());
        }
    }

    @Override
    public Optional<Long> getUserIdBySession(WebSocketSession session) {
        SessionInfo sessionInfo = sessionMap.get(session.getId());
        if (sessionInfo == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(sessionInfo.userId());
    }

    @Override
    public Optional<Long> getMeetingIdBySession(WebSocketSession session) {
        SessionInfo sessionInfo = sessionMap.get(session.getId());
        if (sessionInfo == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(sessionInfo.meetingId());
    }


    @Override
    public boolean existsBySession(WebSocketSession session) {
        return sessionMap.containsKey(session.getId());
    }

    record SessionInfo(Long userId, Long meetingId) {
    }
} 
