package com.jolupbisang.demo.global.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> values;

    // 기존 Map 방식 (하위호환성)
    public CustomException(ErrorCode errorCode, Map<String, Object> values, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.values = values;
    }

    public CustomException(ErrorCode errorCode, Map<String, Object> values) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.values = values;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.values = Map.of();
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.values = Map.of();
    }

    // 새로운 String.format 방식
    public CustomException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(formatMessage(errorCode.getMessage(), detailedMessage, args));
        this.errorCode = errorCode;
        this.values = Map.of();
    }

    public CustomException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(formatMessage(errorCode.getMessage(), detailedMessage, args), cause);
        this.errorCode = errorCode;
        this.values = Map.of();
    }

    private static String formatMessage(String baseMessage, String detailedMessage, Object... args) {
        if (detailedMessage == null || detailedMessage.isEmpty()) {
            return baseMessage;
        }
        try {
            String formattedDetail = args.length > 0 ? String.format(detailedMessage, args) : detailedMessage;
            return baseMessage + " - " + formattedDetail;
        } catch (Exception e) {
            return baseMessage + " - " + detailedMessage + " [format error: " + java.util.Arrays.toString(args) + "]";
        }
    }
}

