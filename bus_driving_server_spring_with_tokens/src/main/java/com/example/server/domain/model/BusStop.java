package com.example.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {
    private int id;
    private String name;
    private Point location;
    private List<BusLine> busLines;

    public BusStop(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
