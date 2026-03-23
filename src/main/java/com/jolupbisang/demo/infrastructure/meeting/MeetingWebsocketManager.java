package com.jolupbisang.demo.infrastructure.meeting;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jolupbisang.demo.infrastructure.websocket.WebsocketManager;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponse;
import com.jolupbisang.demo.presentation.audio.dto.response.SocketResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class MeetingWebsocketManager {

    private final Map<Long, WebsocketManager> websocketManager = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public void addToMeeting(long meetingId, long userId, WebSocketSession session) {
        websocketManager.computeIfAbsent(meetingId, k -> new WebsocketManager(objectMapper))
                .add(userId, session);
    }

    public void removeUserOfMeeting(long meetingId, long userId) {
        WebsocketManager manager = websocketManager.get(meetingId);
        if (manager != null) {
            manager.remove(userId);
        }
    }

    public void removeAllUsersOfMeeting(long meetingId) {
        WebsocketManager manager = websocketManager.get(meetingId);
        if (manager != null) {
            manager.removeAll();
        }
    }

    public void closeSessionOfMeeting(long meetingId, long userId) {
        WebsocketManager manager = websocketManager.get(meetingId);
        if (manager != null) {
            manager.close(userId);
        }
    }

    public void closeAllSessionOfMeeting(long meetingId) {
        WebsocketManager manager = websocketManager.get(meetingId);
        if (manager != null) {
            manager.closeAll();
        }
    }

    public void sendToMeeting(long meetingId, SocketResponseType type, Object message) {
        WebsocketManager manager = websocketManager.get(meetingId);
        if (manager != null) {
            manager.broadcast(SocketResponse.of(type, message));
        }
    }

    public void sendToUserOfMeeting(long meetingId, long userId, SocketResponseType type, Object message) {
        WebsocketManager manager = websocketManager.get(meetingId);
        if (manager != null) {
            manager.send(userId, SocketResponse.of(type, message));
        }
    }
}
