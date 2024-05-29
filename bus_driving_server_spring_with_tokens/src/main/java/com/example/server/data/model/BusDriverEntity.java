package com.example.server.data.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bus_driver")
public class BusDriverEntity {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus_driver", nullable = false)
    private int id;
    @Column(name = "driver_first_name")
    private String firstName;
    @Column(name = "driver_last_name")
    private String lastName;
    @Column(name = "driver_phone")
    private String phone;
    @ManyToOne
    @JoinColumn(name = "assigned_bus_line", referencedColumnName = "id_bus_line")
    private BusLineEntity assignedLine;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_bus_driver")
    private DriverCredentialEntity credential;

    public BusDriverEntity(int id, String firstName, String lastName, String phone, BusLineEntity assignedLine) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.assignedLine = assignedLine;
    }
}
