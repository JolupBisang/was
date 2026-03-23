package com.jolupbisang.demo.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String detailedMessage, Object... args) {
        super(formatMessage(errorCode.getMessage(), detailedMessage, args));
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause, String detailedMessage, Object... args) {
        super(formatMessage(errorCode.getMessage(), detailedMessage, args), cause);
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    public String getCustomCode() {
        return errorCode.getCode();
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

