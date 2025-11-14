package com.jolupbisang.demo.infrastructure.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
public class WebsocketManager {
    private final Map<Long, NonBlockingWebsocketSender> websocketSenders = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public void add(long key, WebSocketSession session) {
        NonBlockingWebsocketSender websocketSender = new NonBlockingWebsocketSender(session);
        websocketSenders.put(key, websocketSender);
    }

    public void remove(long key) {
        websocketSenders.remove(key);
    }

    public void removeAll() {
        websocketSenders.clear();
    }

    public void close(long userId) {
        NonBlockingWebsocketSender sender = websocketSenders.get(userId);

        if (sender != null) {
            sender.closeQuietly();
        }
    }

    public void closeAll() {
        for (NonBlockingWebsocketSender sender : websocketSenders.values()) {
            sender.closeQuietly();
        }
    }

    public void broadcast(Object message) {
        ArrayList<NonBlockingWebsocketSender> senders = new ArrayList<>(websocketSenders.values());

        for (NonBlockingWebsocketSender sender : senders) {
            String jsonMessage = makeMessageToString(message);
            if (jsonMessage != null) {
                sender.send(message);
            }
        }
    }

    public void send(long key, Object message) {
        NonBlockingWebsocketSender sender = websocketSenders.get(key);

        if (sender != null) {
            String jsonMessage = makeMessageToString(message);
            if (jsonMessage != null) {
                sender.send(jsonMessage);
            }
        }
    }

    private String makeMessageToString(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            log.error("Failed to serialize message to string: {}", message, e);
            return null;
        }
    }
}
