package com.dev.imageapi.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ExceptionResponse {
    private LocalDate date;
    private String message;
}
