package com.jolupbisang.demo.domain.meeting;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingStatus {
    WAITING, IN_PROGRESS, COMPLETED, CANCELLED;
}
