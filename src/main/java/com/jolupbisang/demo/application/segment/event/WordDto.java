package com.jolupbisang.demo.application.segment.event;

public record WordDto(
        int start,
        int end,
        String text,
        String lang
) {
}
