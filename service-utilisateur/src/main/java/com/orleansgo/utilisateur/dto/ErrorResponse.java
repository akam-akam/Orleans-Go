package com.orleansgo.utilisateur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private HttpStatus status;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
