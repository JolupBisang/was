package com.jolupbisang.demo.global.exception;

import com.jolupbisang.demo.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        String errorId = UUID.randomUUID().toString();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(errorId, "Bad Request", errors));
    }

    @ExceptionHandler(InfraException.class)
    protected ResponseEntity<?> handleInfraException(InfraException ex) {
        String errorId = UUID.randomUUID().toString();
        log.error("[Infra Error - {}] - errorCode: {}, status: {}, message: {}", errorId, ex.getCustomCode(), ex.getStatus(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(errorId, "일시적인 오류 입니다. 잠시후 다시 시도해주세요. 지속될시 문의해주세요"));
    }

    @ExceptionHandler(DomainException.class)
    protected ResponseEntity<?> handleDomainException(DomainException ex) {
        String errorId = UUID.randomUUID().toString();
        log.error("[Domain Error - {}] - errorCode: {}, status: {}, message: {}", errorId, ex.getCustomCode(), ex.getStatus(), ex.getMessage(), ex);

        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.of(errorId, ex.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException ex) {
        String errorId = UUID.randomUUID().toString();
        log.error("[Application Error - {}] - errorCode: {}, status: {}, message: {}", errorId, ex.getCustomCode(), ex.getStatus(), ex.getMessage(), ex);

        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.of(errorId, ex.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handleCustomException(CustomException ex) {
        String errorId = UUID.randomUUID().toString();
        log.error("[Custom Error - {}] - errorCode: {}, status: {}, message: {}", errorId, ex.getCustomCode(), ex.getStatus(), ex.getMessage(), ex);

        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.of(errorId, "Internal Server Error."));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(Exception ex) {
        String errorId = UUID.randomUUID().toString();
        log.error("[Custom Error - {}] - message: {}", errorId, ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(errorId, "Internal Server Error"));
    }
}

