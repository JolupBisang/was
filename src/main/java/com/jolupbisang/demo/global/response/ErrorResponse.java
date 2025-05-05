package com.jolupbisang.demo.global.response;

import java.util.Map;

public record ErrorResponse(String message, String errorId, Map<String, String> errors) {

    public static ErrorResponse of(String message, String errorId) {
        return new ErrorResponse(message, errorId, null);
    }

    public static ErrorResponse of(String message, String errorId, Map<String, String> errors) {
        return new ErrorResponse(message, errorId, errors);
    }
}
