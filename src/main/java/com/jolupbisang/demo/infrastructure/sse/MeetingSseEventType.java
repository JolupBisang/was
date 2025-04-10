package com.jolupbisang.demo.infrastructure.sse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingSseEventType {
    PARTICIPATION_RATE, FEEDBACK, SUMMARY;
}
