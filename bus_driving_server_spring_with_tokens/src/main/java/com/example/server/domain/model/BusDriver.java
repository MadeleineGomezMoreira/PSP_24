package com.example.server.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusDriver {

    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private BusLine assignedLine;
    private DriverCredential credential;

    public BusDriver(int id, String firstName, String lastName, String phone, BusLine assignedLine) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.assignedLine = assignedLine;
    }

}
