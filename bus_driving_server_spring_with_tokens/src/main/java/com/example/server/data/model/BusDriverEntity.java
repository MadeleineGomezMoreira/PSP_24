package com.example.server.data.model;

import com.example.server.domain.model.BusLine;
import com.example.server.domain.model.DriverCredential;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus_driver", nullable = false)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @ManyToOne
    @JoinColumn(name = "assigned_bus_line", referencedColumnName = "id_bus_line")
    private BusLineEntity assignedLine;
    @OneToOne
    @JoinColumn(name = "credential_id", referencedColumnName = "credential_id")
    private DriverCredentialEntity credential;

}
