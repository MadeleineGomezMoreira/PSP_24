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
@Table(name = "bus_stop")
public class BusStopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus_stop", nullable = false)
    private int id;
    @Column(name = "stop_name")
    private String name;
    @Column(name = "x_coordinate")
    private double xCoordinate;
    @Column(name = "y_coordinate")
    private double yCoordinate;

    @ManyToMany(mappedBy = "busStops")
    private List<BusLineEntity> busLines;

}
