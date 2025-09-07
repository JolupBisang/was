package com.jolupbisang.demo.global.exception;

import com.jolupbisang.demo.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

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

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public void handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpServletRequest request) {
        log.warn("Async request timed out for URI: {}", request.getRequestURI());
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

        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode.getMessage(), errorId, errors));
    }
}

