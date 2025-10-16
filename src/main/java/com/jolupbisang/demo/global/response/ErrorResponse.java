package com.jolupbisang.demo.global.response;

import java.util.Map;

public record ErrorResponse(
        String errorId,
        String message,
        Map<String, String> errors
) {

    public static ErrorResponse of(String errorId, String message) {
        return new ErrorResponse(errorId, message, null);
    }

    public static ErrorResponse of(String errorId, String message, Map<String, String> errors) {
        return new ErrorResponse(errorId, message, errors);
    }
}
