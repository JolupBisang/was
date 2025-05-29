package com.jolupbisang.demo.application.meeting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StepFunctionOutput(
    String statusCode,
    List<MergedPath> paths
) {

    public record MergedPath(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("s3_path")
        String s3Path
    ) {
    }
}
