package com.jolupbisang.demo.infrastructure.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class MeetingSseManager {

    private final ObjectMapper objectMapper;
    private final Map<Long, Map<Long, SseEmitter>> meetingEmitters = new ConcurrentHashMap<>();

    private static final long SEE_EMITTER_TIMEOUT = 10 * 60 * 1000; //10 minutes
    private static final String CONNECTED_MESSAGE = "Connected To Meeting Sse Event Stream";

    public SseEmitter subscribe(long meetingId, long userId) {
        meetingEmitters.putIfAbsent(meetingId, new ConcurrentHashMap<>());

        SseEmitter emitter = createEmitter(meetingId, userId);
        meetingEmitters.get(meetingId).put(userId, emitter);
        sendEvent(meetingId, userId, MeetingSseEventType.CONNECTED, CONNECTED_MESSAGE);

        return emitter;
    }

    public void sendEvent(long meetingId, MeetingSseEventType eventType, Object data) {
        if (!meetingEmitters.containsKey(meetingId)) {
            log.error("해당 회의에 연결된 Sse 클라이언트가 없습니다. meetingId: {}", meetingId);
            return;
        }

        for (SseEmitter emitter : meetingEmitters.get(meetingId).values()) {
            sendEventByUsingEmitter(emitter, eventType, data);
        }
    }

    public void sendEvent(long meetingId, long userId, MeetingSseEventType eventType, Object data) {
        if (!meetingEmitters.containsKey(meetingId)) {
            log.error("해당 회의에 연결된 Sse 클라이언트가 없습니다. meetingId: {}, 보내려던 userId {}", meetingId, userId);
            return;
        }

        SseEmitter userEmitter = meetingEmitters.get(meetingId).get(userId);
        sendEventByUsingEmitter(userEmitter, eventType, data);
    }

    private SseEmitter createEmitter(long meetingId, long userId) {
        SseEmitter emitter = new SseEmitter(SEE_EMITTER_TIMEOUT);
        emitter.onCompletion(() -> {
            meetingEmitters.get(meetingId).remove(userId);
            if (meetingEmitters.get(meetingId).isEmpty()) {
                meetingEmitters.remove(meetingId);
            }
        });

        emitter.onTimeout(() -> {
            log.info("[{}]: meetingId {}, userId {} SSE connection timed out", emitter, meetingId, userId);
            emitter.complete();
        });

        emitter.onError((ex) -> {
            log.info("[{}]: meetingId {}, userId {} SSE connection error", emitter, meetingId, userId, ex);
            emitter.complete();
        });

        return emitter;
    }

    private void sendEventByUsingEmitter(SseEmitter sseEmitter, MeetingSseEventType eventType, Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name(eventType.toString())
                    .data(objectMapper.writeValueAsString(data))
            );
        } catch (JsonProcessingException e) {
            log.error("[Sse Emitter Error] eventType: {}, data: {}]", eventType, data, e);
        } catch (IOException e) {
            sseEmitter.completeWithError(e);
        }
    }
}

