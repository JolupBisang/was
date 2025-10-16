package com.jolupbisang.demo.infrastructure.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
public class NonBlockingWebsocketSender {
    private final WebSocketSession session;
    private final Queue<Object> queue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean writing = new AtomicBoolean(false);

    @Async("websocketExecutor")
    public void send(Object msg) {
        queue.add(msg);
        drain();
    }

    public void closeQuietly() {
        try {
            session.close();
        } catch (IOException ignore) {
        }
        queue.clear();
        writing.set(false);
    }

    private void drain() {
        if (!writing.compareAndSet(false, true)) return;

        while (true) {
            write();
            writing.set(false);
            if (queue.isEmpty()) {
                break;
            }
            if (!writing.compareAndSet(false, true)) { //원소가 있다면 Writer 도전 -> 실패하면 끝
                break;
            }
        }
    }

    private void write() {
        while (true) {
            Object next = queue.poll();
            if (next == null) {
                return;
            }
            sendMessage(session, next);
        }
    }

    private void sendMessage(WebSocketSession session, Object message) {
        try {
            if (message instanceof ByteBuffer) {
                session.sendMessage(new BinaryMessage((ByteBuffer) message));
            } else if (message instanceof String) {
                session.sendMessage(new TextMessage((String) message));
            } else {
                log.error("Unsupported message type: {}", message.getClass().getName());
            }
        } catch (IOException e) {
            log.error("Failed to send message to WebSocket session. Cause: {}", e.getMessage(), e);
            closeQuietly();
        }
    }
}
