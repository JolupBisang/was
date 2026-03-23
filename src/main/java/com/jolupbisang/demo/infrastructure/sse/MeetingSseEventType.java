package com.jolupbisang.demo.infrastructure.sse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingSseEventType {
    CONNECTED, PARTICIPATION_RATE, FEEDBACK, SUMMARY;
}
