package com.example.projet_gestion_absences.exception;

// src/main/java/com/example/projet_gestion_absences/exception/ApiError.java
//package com.example.projet_gestion_absences.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

public class ApiError {
    private HttpStatus status;
    private String message;
    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp = OffsetDateTime.now();

    public ApiError(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public OffsetDateTime getTimestamp() { return timestamp; }
}
