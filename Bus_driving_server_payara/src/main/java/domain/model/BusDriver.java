package domain.model;

import lombok.Data;

@Data
public class BusDriver {

    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private BusLine assignedLine;
    private DriverCredential credential;

    public BusDriver() {
    }

    public BusDriver(String firstName) {
        this.firstName = firstName;
    }

    public BusDriver(int id, String firstName, String lastName, String phone, BusLine assignedLine) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.assignedLine = assignedLine;
    }

    public BusDriver(String firstName, String lastName, String phone, BusLine assignedLine) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.assignedLine = assignedLine;
    }

    public BusDriver(String firstName, String lastName, String phone, BusLine assignedLine, DriverCredential credential) {
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.assignedLine = assignedLine;
        this.credential = credential;
    }

    public BusDriver(int id) {
        this.id = id;
    }
}
