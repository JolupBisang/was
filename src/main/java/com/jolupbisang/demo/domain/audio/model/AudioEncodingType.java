package com.jolupbisang.demo.domain.audio.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AudioEncodingType {
    AUDIO_MP4("audio/mp4");

    private final String type;

    public static AudioEncodingType fromString(String type) {
        for (AudioEncodingType encodingType : AudioEncodingType.values()) {
            if (encodingType.getType().equals(type)) {
                return encodingType;
            }
        }
        return null;
    }
}
