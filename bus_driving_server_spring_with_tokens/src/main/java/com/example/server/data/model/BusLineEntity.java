package com.example.server.data.model;

import jakarta.persistence.*;
import lombok.*;

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

}


