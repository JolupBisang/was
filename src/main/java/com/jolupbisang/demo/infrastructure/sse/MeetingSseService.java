package com.jolupbisang.demo.infrastructure.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeetingSseService {

    private final ObjectMapper objectMapper;
    //첫번째 key: meetingId, 두번째 key: userId + "_" + eventType
    private final Map<String, Map<String, SseEmitter>> meetingEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String meetingId, String userId, MeetingSseEventType eventType) {
        meetingEmitters.putIfAbsent(meetingId, new ConcurrentHashMap<>());

        String emitterKey = userId + "_" + eventType;
        SseEmitter emitter = createEmitter(meetingId, emitterKey);
        sendConnectionMessage(meetingId, eventType, emitter);
        meetingEmitters.get(meetingId).put(emitterKey, emitter);

        return emitter;
    }

    public void sendEventToMeeting(String meetingId, MeetingSseEventType eventType, Object data) {
        if (!meetingEmitters.containsKey(meetingId)) {
            log.error("연결된 클라이언트가 없습니다. meetingId: {}", meetingId);
            return;
        }

        Map<String, SseEmitter> emitters = meetingEmitters.get(meetingId);
        emitters.forEach((key, emitter) -> {
            if (key.endsWith("_" + eventType)) {
                try {
                    emitter.send(SseEmitter.event()
                            .name(eventType.toString())
                            .data(objectMapper.writeValueAsString(data)));
                } catch (JsonProcessingException e) {
                    log.error("[Whisper Client SSE Error] meetingId: {}, userId: {}, eventType: {}, data: {}", meetingId, key, eventType, data, e);
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        });
    }

    public void sendEventToUserOfMeeting(String meetingId, String userId, MeetingSseEventType eventType, Object data) {
        if (!meetingEmitters.containsKey(meetingId) || !meetingEmitters.get(meetingId).containsKey(userId + "_" + eventType)) {
            log.error("연결된 클라이언트가 없습니다. meetingId: {}", meetingId);
            return;
        }

        SseEmitter userEmitter = meetingEmitters.get(meetingId).get(userId + "_" + eventType);
        try {
            userEmitter.send(SseEmitter.event()
                    .name(eventType.toString())
                    .data(objectMapper.writeValueAsString(data)));
        } catch (JsonProcessingException e) {
            log.error("[Whisper Client SSE Error] meetingId: {}, userId: {}, eventType: {}, data: {}", meetingId, userId, eventType, data, e);
        } catch (IOException e) {
            userEmitter.completeWithError(e);
        }

    }

    private SseEmitter createEmitter(String meetingId, String emitterKey) {
        SseEmitter emitter = new SseEmitter(180000L);
        emitter.onCompletion(() -> {
            meetingEmitters.get(meetingId).remove(emitterKey);
            if (meetingEmitters.get(meetingId).isEmpty()) {
                meetingEmitters.remove(meetingId);
            }
        });

        emitter.onTimeout(() -> {
            log.info("[{}]: meetingId {} SSE connection timed out", emitter, meetingId);
            emitter.complete();
        });

        emitter.onError((ex) -> {
            log.info("[{}]: meetingId {} SSE connection error", emitter, meetingId, ex);
            emitter.complete();
        });

        return emitter;
    }

    private void sendConnectionMessage(String meetingId, MeetingSseEventType eventType, SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event()
                    .name("CONNECT")
                    .data("connected to " + eventType + " event stream"));
        } catch (IOException e) {
            log.info("[{}]: meetingId {} SSE IOException error", emitter, meetingId, e);
            emitter.completeWithError(e);
        }
    }
}
