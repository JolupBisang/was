package com.jolupbisang.demo.domain.audio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AudioEncodingType {
    AUDIO_MP4("audio/mp4", ".mp4"), AUDIO_PCM("audio/pcm", ".pcm");

    private final String type;
    private final String extension;

    public static AudioEncodingType fromString(String type) {
        for (AudioEncodingType encodingType : AudioEncodingType.values()) {
            if (encodingType.getType().equals(type)) {
                return encodingType;
            }
        }
        return null;
    }
}
