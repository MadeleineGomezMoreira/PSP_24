package org.example.security_project_encripting.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppError {

    private String message;
    private LocalDateTime localDate;

    public AppError(String message) {
        this.message = message;
        this.localDate = LocalDateTime.now();
    }
}
