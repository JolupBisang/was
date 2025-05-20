package com.jolupbisang.demo.infrastructure.meeting.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MeetingSessionRepositoryImpl implements MeetingSessionRepository {

    private final Map<WebSocketSession, SessionInfo> sessions = new ConcurrentHashMap<>();

    private record SessionInfo(Long userId, Long meetingId) {
    }

    @Override
    public void save(WebSocketSession session, Long userId, Long meetingId) {
        SessionInfo sessionInfo = new SessionInfo(userId, meetingId);
        sessions.put(session, sessionInfo);
        log.debug("Session saved: sessionId={}, userId={}, meetingId={}", session.getId(), userId, meetingId);
    }

    @Override
    public void delete(WebSocketSession session) {
        SessionInfo removed = sessions.remove(session);
        if (removed != null) {
            log.debug("Session deleted: sessionId={}, userId={}, meetingId={}", session.getId(), removed.userId(), removed.meetingId());
        } else {
            log.debug("Session not found for deletion: sessionId={}", session.getId());
        }
    }

    @Override
    public Optional<Long> getUserIdBySession(WebSocketSession session) {
        return Optional.ofNullable(sessions.get(session)).map(SessionInfo::userId);
    }

    @Override
    public Optional<Long> getMeetingIdBySession(WebSocketSession session) {
        return Optional.ofNullable(sessions.get(session)).map(SessionInfo::meetingId);
    }

    @Override
    public Optional<WebSocketSession> findByUserId(Long userId) {
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().userId().equals(userId))
                .map(Map.Entry::getKey)
                .findFirst(); // 사용자는 하나의 세션만 가진다고 가정
    }

    @Override
    public void deleteByUserId(Long userId) {
        List<WebSocketSession> sessionsToDelete = new ArrayList<>();
        for (Map.Entry<WebSocketSession, SessionInfo> entry : sessions.entrySet()) {
            if (entry.getValue().userId().equals(userId)) {
                sessionsToDelete.add(entry.getKey());
            }
        }
        sessionsToDelete.forEach(this::delete);
    }

    @Override
    public List<WebSocketSession> findAllByMeetingId(Long meetingId) {
        return sessions.entrySet().stream()
                .filter(entry -> entry.getValue().meetingId().equals(meetingId))
                .map(Map.Entry::getKey)
                .toList();
    }
} 

