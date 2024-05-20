package com.example.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusLine {
    private int id;
    private String lineStart;
    private String lineEnd;

    public BusLine(int id) {
        this.id = id;
    }
}


