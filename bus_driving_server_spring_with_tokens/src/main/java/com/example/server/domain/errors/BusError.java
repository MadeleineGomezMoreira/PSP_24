package com.example.server.domain.errors;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BusError {
    private String message;
    private final LocalDateTime date;

    public BusError(String message) {
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
