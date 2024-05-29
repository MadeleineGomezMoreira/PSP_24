package com.example.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusLine {
    private int id;
    private String lineStart;
    private String lineEnd;
    private List<BusStop> busStops;

    public BusLine(int id) {
        this.id = id;
    }

    public BusLine(int id, String lineStart, String lineEnd) {
        this.id = id;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
    }
}


