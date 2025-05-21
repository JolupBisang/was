package com.jolupbisang.demo.infrastructure.meeting.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

public interface MeetingSessionRepository {
    void save(WebSocketSession session, Long userId, Long meetingId);

    void delete(WebSocketSession session);

    Optional<Long> getUserIdBySession(WebSocketSession session);

    Optional<Long> getMeetingIdBySession(WebSocketSession session);

    Optional<WebSocketSession> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    List<WebSocketSession> findAllByMeetingId(Long meetingId);
}
