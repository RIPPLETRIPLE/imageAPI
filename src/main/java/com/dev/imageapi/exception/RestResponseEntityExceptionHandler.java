package com.dev.imageapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<ExceptionResponse> handleException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder()
                .message(exception.getMessage())
                .date(LocalDate.now())
                .build());
    }
}
