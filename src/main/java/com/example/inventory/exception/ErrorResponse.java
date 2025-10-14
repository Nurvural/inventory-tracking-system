package com.example.inventory.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ErrorResponse {
    private final int status;               // HTTP status code (404, 400, 500)
    private final String error;             // HTTP status mesajı (Not Found, Bad Request)
    private final ErrorCode code;           // Bizim enum
    private final String message;           // Exception mesajı
    private final String path;              // Request URI
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();  // Zaman damgası
}
