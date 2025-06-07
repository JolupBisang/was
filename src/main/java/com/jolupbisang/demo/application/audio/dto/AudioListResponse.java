package com.jolupbisang.demo.application.audio.dto;

import java.util.List;

public record AudioListResponse(
        List<AudioInfo> audioList
) {
    public record AudioInfo(
            Long userId,
            String presignedUrl
    ) {}
} 