package com.jolupbisang.demo.application.audio.query.dto;

import com.jolupbisang.demo.domain.audio.model.FullAudio;

import java.util.List;

public record AudioListRes(
        List<AudioInfo> audioList
) {
    public static AudioListRes from(List<FullAudio> fullAudios) {
        return new AudioListRes(
                fullAudios.stream()
                        .map(fullAudio -> new AudioInfo(fullAudio.getUserId(), fullAudio.getAudioUrl()))
                        .toList()
        );
    }

    public record AudioInfo(
            Long userId,
            String presignedUrl
    ) {
    }
} 
