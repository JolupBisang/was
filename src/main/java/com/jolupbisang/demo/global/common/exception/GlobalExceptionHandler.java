package com.jolupbisang.demo.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException ex) {
        return buildErrorResponse(ex, ex.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception ex) {
        return buildErrorResponse(ex, GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, ErrorCode errorCode) {
        String errorId = UUID.randomUUID().toString();
        log.error("[{}] Exception occurred: {}", errorId, ex.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getMessage(), errorId));
    }

    private record ErrorResponse(String message, String errorId) {
    }
}

