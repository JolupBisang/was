package com.jolupbisang.demo.application.event;

import com.jolupbisang.demo.infrastructure.sse.MeetingSseEventType;

public record SseEmitEvent(
        String meetingId,
        MeetingSseEventType type,
        Object data
) {

}
