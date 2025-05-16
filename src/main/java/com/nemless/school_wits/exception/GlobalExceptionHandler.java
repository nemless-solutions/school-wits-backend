package com.nemless.school_wits.exception;

import com.nemless.school_wits.config.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseMessage.UNABLE_TO_SAVE);
    }
}
