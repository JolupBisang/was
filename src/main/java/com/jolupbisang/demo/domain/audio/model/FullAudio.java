package com.jolupbisang.demo.domain.audio.model;

import lombok.Getter;

@Getter
public class FullAudio {
    long meetingId;
    long userId;
    String audioUrl;

    public FullAudio(long meetingId, long userId, String audioUrl) {
        this.meetingId = meetingId;
        this.userId = userId;
        this.audioUrl = audioUrl;
    }
}
