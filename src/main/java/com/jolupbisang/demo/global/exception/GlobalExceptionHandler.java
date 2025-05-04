package com.jolupbisang.demo.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException ex) {
        return buildErrorResponse(ex, ex.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildErrorResponse(e, GlobalErrorCode.INVALID_INPUT, errors);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception ex) {
        return buildErrorResponse(ex, GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, ErrorCode errorCode) {
        return buildErrorResponse(ex, errorCode, null);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, ErrorCode errorCode, Map<String, String> errors) {
        String errorId = UUID.randomUUID().toString();
        log.error("[{}] Exception occurred: {}", errorId, errorCode.getMessage(), ex);

        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode.getMessage(), errorId, errors));
    }

    private record ErrorResponse(String message, String errorId, Map<String, String> errors) {
    }
}

