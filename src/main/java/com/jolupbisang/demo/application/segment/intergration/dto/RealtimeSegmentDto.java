package com.jolupbisang.demo.application.segment.intergration.dto;

import java.time.LocalDateTime;

public record RealtimeSegmentDto(
        LocalDateTime spokenTime,
        LocalDateTime translatedTime,
        long userId,
        int order,
        String text
) {
}
