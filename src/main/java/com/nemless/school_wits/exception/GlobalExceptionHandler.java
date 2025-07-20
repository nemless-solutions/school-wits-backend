package com.nemless.school_wits.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.nemless.school_wits.config.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestException(BadRequestException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<String> internalServerException(InternalServerException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedException(UnauthorizedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<String> databaseException(PSQLException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseMessage.UNABLE_TO_PERFORM_DATABASE_OPERATION);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validationException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        String message = "Malformed or invalid request body";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();
            Object invalidValue = ife.getValue();

            if (targetType.isEnum()) {
                message = String.format("Invalid value '%s' for enum type %s. Allowed values: %s",
                        invalidValue,
                        targetType.getSimpleName(),
                        Arrays.toString(targetType.getEnumConstants()));
            } else {
                message = String.format("Invalid value '%s' for field of type %s",
                        invalidValue,
                        targetType.getSimpleName());
            }
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", message));
    }
}
