package com.example.server.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bus_line")
public class BusLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus_line", nullable = false)
    private int id;
    @Column(name = "line_start")
    private String lineStart;
    @Column(name = "line_end")
    private String lineEnd;

    @ManyToMany
    @JoinTable(
            name = "line_stops",
            joinColumns = @JoinColumn(name = "id_line"),
            inverseJoinColumns = @JoinColumn(name = "id_stop")
    )
    private List<BusStopEntity> busStops;

    public BusLineEntity(int id, String lineStart, String lineEnd) {
        this.id = id;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
    }
}


